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
package org.htmlunit.javascript.host.html;

import static java.nio.charset.StandardCharsets.ISO_8859_1;
import static java.nio.charset.StandardCharsets.UTF_8;
import static org.junit.jupiter.api.Assertions.fail;

import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import org.apache.http.client.utils.DateUtils;
import org.htmlunit.BrowserVersion;
import org.htmlunit.WebDriverTestCase;
import org.htmlunit.html.HtmlPage;
import org.htmlunit.junit.annotation.Alerts;
import org.htmlunit.junit.annotation.BuggyWebDriver;
import org.htmlunit.junit.annotation.HtmlUnitNYI;
import org.htmlunit.util.MimeType;
import org.htmlunit.util.NameValuePair;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;

/**
 * Tests for {@link HTMLDocument}.
 *
 * @author Ahmed Ashour
 * @author Marc Guillemot
 * @author Ronald Brill
 * @author Frank Danek
 */
public class HTMLDocumentTest extends WebDriverTestCase {
    /** jQuery selectors that aren't CSS selectors. */
    static final String[] JQUERY_CUSTOM_SELECTORS = {"div.submenu-last:last",
        "*#__sizzle__ div.submenu-last:last", "div:animated", "div:animated", "*:button", "*:checkbox", "div:even",
        "*:file", "div:first", "td:gt(4)", ":header", ":hidden", ":image", ":input", "td:lt(4)",
        ":odd", ":password", ":radio", ":reset", ":selected", ":submit", ":text", ":visible"
    };

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("[object HTMLDocument]")
    public void scriptableToString() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    log(document);\n"
            + "  }\n"
            + "</script></head>\n"
            + "<body onload='test()'>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"2", "DIV", "2"})
    public void getElementsByTagName() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html>\n"
            + "<head>\n"
            + "  <script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    log(document.getElementsByTagName('div').length);\n"
            + "    document.getElementById('myDiv').innerHTML = \"<P><DIV id='secondDiv'></DIV></P>\";\n"
            + "    log(document.getElementById('secondDiv').nodeName);\n"
            + "    log(document.getElementsByTagName('div').length);\n"
            + "  }\n"
            + "  </script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "<div id='myDiv'>\n"
            + "  <div></div>\n"
            + "</div>\n"
            + "</body>\n"
            + "</html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"function", "div1", "span2", "span3", "2", "1", "1", "0", "0", "0"})
    public void getElementsByClassName() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "function doTest() {\n"
            + "  log(typeof document.getElementsByClassName);\n"
            + "  try {\n"
            + "    var elements = document.getElementsByClassName('foo');\n"
            + "    for (var i = 0; i < elements.length; i++) {\n"
            + "      log(elements[i].id);\n"
            + "    }\n"
            + "    log(document.getElementsByClassName('red').length);\n"
            + "    log(document.getElementsByClassName('foo red').length);\n"
            + "    log(document.getElementsByClassName('red foo').length);\n"
            + "    log(document.getElementsByClassName('blue foo').length);\n"
            + "    log(document.getElementsByClassName('*').length);\n"
//            + "    log(document.getElementsByClassName().length);\n" // exception in FF3
            + "    log(document.getElementsByClassName(null).length);\n"
            + "  }\n"
            + "  catch(e) { logEx(e) }\n"
            + "}\n"
            + "</script></head><body onload='doTest()'>\n"
            + "<div class='foo' id='div1'><span class='c2'>hello</span>\n"
            + "  <span class='foo' id='span2'>World!</span></div>\n"
            + "<span class='foo red' id='span3'>again</span>\n"
            + "<span class='red' id='span4'>bye</span>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
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
    public void compatMode_doctype() throws Exception {
        compatMode("<!DOCTYPE>");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("CSS1Compat")
    public void compatMode_html() throws Exception {
        compatMode("<!DOCTYPE html>");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("CSS1Compat")
    public void compatMode_htmlLowercase() throws Exception {
        compatMode("<!doctype html>");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("BackCompat")
    public void compatMode_question() throws Exception {
        compatMode("<?DOCTYPE html>");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("BackCompat")
    public void compatMode_html_transitional_40_noUrl() throws Exception {
        compatMode("<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.0 Transitional//EN\">");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("BackCompat")
    public void compatMode_html_transitional_noUrl() throws Exception {
        compatMode("<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\">");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("BackCompat")
    public void compatMode_html_transitional_40() throws Exception {
        compatMode("<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.0 Transitional//EN\" "
            + "\"http://www.w3.org/TR/html4/loose.dtd\">");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("CSS1Compat")
    public void compatMode_html_transitional() throws Exception {
        compatMode("<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\" "
            + "\"http://www.w3.org/TR/html4/loose.dtd\">");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("CSS1Compat")
    public void compatMode_html_strict_40() throws Exception {
        compatMode("<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.0//EN\" \"http://www.w3.org/TR/html4/strict.dtd\">");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("CSS1Compat")
    public void compatMode_html_strict() throws Exception {
        compatMode("<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01//EN\" \"http://www.w3.org/TR/html4/strict.dtd\">");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("CSS1Compat")
    public void compatMode_xhtml_transitional() throws Exception {
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
        final String html = doctype
            + "<html>\n"
            + "<head>\n"
            + "  <script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    log(document.compatMode);\n"
            + "  }\n"
            + "  </script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "</body>\n"
            + "</html>";

        final WebDriver driver = loadPageVerifyTitle2(html);
        if (driver instanceof HtmlUnitDriver) {
            final HtmlPage page = (HtmlPage) getEnclosedPage();
            assertEquals("BackCompat".equals(getExpectedAlerts()[0]), page.isQuirksMode());
        }
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    public void uniqueID() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html>\n"
            + "<head>\n"
            + "  <script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    log(document.uniqueID != undefined);\n"
            + "  }\n"
            + "  </script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "</body>\n"
            + "</html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"[object HTMLDivElement]", "[object HTMLUnknownElement]", "[object Element]"})
    public void createDocumentNS() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html>\n"
            + "<head>\n"
            + "<script>\n"
            + "function test() {\n"
            + LOG_TITLE_FUNCTION
            + "  var elt = document.createElementNS('http://www.w3.org/1999/xhtml', 'div');\n"
            + "  log(elt);\n"
            + "  var elt = document.createElementNS('http://www.w3.org/1999/xhtml', 'foo');\n"
            + "  log(elt);\n"
            + "  elt = document.createElementNS('blabla', 'div');\n"
            + "  log(elt);\n"
            + "}\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "</body>\n"
            + "</html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("[object SVGSVGElement]")
    public void createDocumentNS_svg() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><body>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "try {\n"
            + "  var elt = document.createElementNS('http://www.w3.org/2000/svg', 'svg');\n"
            + "  log(elt);\n"
            + "} catch(e) { logEx(e); }\n"
            + "</script></body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("[object SVGRectElement]")
    public void createElementNS() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    log(document.createElementNS('http://www.w3.org/2000/svg', 'rect'));\n"
            + "  }\n"
            + "</script>\n"
            + "</head><body onload='test()'>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "TypeError",
            FF = "NS_ERROR_NOT_AVAILABLE/Exception",
            FF_ESR = "NS_ERROR_NOT_AVAILABLE/Exception")
    @HtmlUnitNYI(FF = "TypeError",
            FF_ESR = "TypeError")
    public void createDocumentNS_xul() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><body>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "try {\n"
            + "  var inner = document.createElementNS('http://www.mozilla.org/keymaster/gatekeeper/there.is.only.xul',"
            + "'label');\n"
            + "  inner.setAttribute('value', 'Hello');\n"
            + "  inner.style['fontFamily'] = 'inherit';\n"
            + "  document.body.appendChild(inner);\n"
            + "  log(document.body.lastChild.value);\n"
            + "}\n"
            + "catch(e) { logEx(e); }\n"
            + "</script>\n"
            + "</body>\n"
            + "</html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void hasXmlNamespaceSupport() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    log(typeof(document.createElementNS) != \"undefined\");\n"
            + "  }\n"
            + "</script></head>\n"
            + "<body onload='test()'>\n"
            + "</body></html>";
        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"[object HTMLCollection]", "0"})
    public void applets() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html>\n"
            + "<head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "function test() {\n"
            + "  log(document.applets);\n"
            + "  log(document.applets.length);\n"
            + "}\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "</body>\n"
            + "</html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"imported: [object HTMLScriptElement]", "replaced"},
            CHROME = {"imported: [object HTMLScriptElement]", "o", "replaced"},
            EDGE = {"imported: [object HTMLScriptElement]", "o", "replaced"})
    @HtmlUnitNYI(CHROME = {"imported: [object HTMLScriptElement]", "replaced"},
            EDGE = {"imported: [object HTMLScriptElement]", "replaced"})
    public void importNode_script() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "function test() {\n"
            + "  try {\n"
            + "    var d = document.implementation.createDocument(null, null, null);\n"
            + "    var xhtml = \"<html xmlns='http://www.w3.org/1999/xhtml'><sc\" "
            + "     + \"ript>log('o'); _scriptEvaluated=true;</scr\" + \"ipt></html>\";\n"
            + "    var newDoc = (new DOMParser()).parseFromString(xhtml, 'text/xml');\n"
            + "    var theScript = newDoc.getElementsByTagName('script')[0];\n"
            + "    var importedScript = window.document.importNode(theScript, true);\n"
            + "    log('imported: ' + importedScript);\n"
            + "    var theSpan = document.getElementById('s1');\n"
            + "    document.body.replaceChild(importedScript, theSpan);\n"
            + "    log('replaced');\n"
            + "  } catch(e) { logEx(e) }\n"
            + "}\n"
            + "</script></head><body onload='test()'>\n"
            + "  <span id='s1'></span>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * This one is like {@link #importNode_script()}, but the script is
     * a child of the imported node.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"imported: [object HTMLDivElement]", "replaced"},
            CHROME = {"imported: [object HTMLDivElement]", "o", "replaced"},
            EDGE = {"imported: [object HTMLDivElement]", "o", "replaced"})
    @HtmlUnitNYI(CHROME = {"imported: [object HTMLDivElement]", "replaced"},
            EDGE = {"imported: [object HTMLDivElement]", "replaced"})
    public void importNode_scriptChild() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "function test() {\n"
            + "  try {\n"
            + "    var d = document.implementation.createDocument(null, null, null);\n"
            + "    var xhtml = \"<html xmlns='http://www.w3.org/1999/xhtml'><div id='myDiv'><sc\" "
            + "     + \"ript>log('o'); _scriptEvaluated=true;</scr\" + \"ipt></div></html>\";\n"
            + "    var newDoc = (new DOMParser()).parseFromString(xhtml, 'text/xml');\n"
            + "    var theDiv = newDoc.getElementById('myDiv');\n"
            + "    var importedDiv = window.document.importNode(theDiv, true);\n"
            + "    log('imported: ' + importedDiv);\n"
            + "    var theSpan = document.getElementById('s1');\n"
            + "    document.body.replaceChild(importedDiv, theSpan);\n"
            + "    log('replaced');\n"
            + "  } catch(e) { logEx(e) }\n"
            + "}\n"
            + "</script></head><body onload='test()'>\n"
            + "  <span id='s1'></span>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("clicked")
    public void dispatchEvent() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function doTest() {\n"
            + "    var e = document.createEvent('MouseEvents');\n"
            + "    e.initMouseEvent('click', true, true, window, 0, 0, 0, 0, 0, false, false, false, false, 0, null);\n"
            + "    document.dispatchEvent(e);\n"
            + "  }\n"
            + "  function clickListener() {\n"
            + "    log('clicked');\n"
            + "  }\n"

            + "  document.addEventListener('click', clickListener, true);\n"
            + "</script>\n"
            + "<body onload='doTest()'>foo</body>\n"
            + "</html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"undefined", "TypeError"})
    public void namespaces() throws Exception {
        final String html =
              "<body><script>\n"
            + LOG_TITLE_FUNCTION
            + "var ns = document.namespaces;\n"
            + "log(ns);\n"
            + "try {\n"
            + "  log(ns.length);\n"
            + "  ns.add('f', 'urn:f');\n"
            + "  log(ns.length);\n"
            + "  log(ns.item(0).name);\n"
            + "  log(ns[0].name);\n"
            + "  log(ns(0).name);\n"
            + "  log(ns('f').name);\n"
            + "  log(ns.item('f').urn);\n"
            + "  log(ns['f'].urn);\n"
            + "  log(ns == document.namespaces);\n"
            + "}\n"
            + "catch(e) { logEx(e) }\n"
            + "</script></body>";

        loadPageVerifyTitle2(html);
    }

    /**
     * Verifies that we can store document methods and use them from a variable (IE only).
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"TypeError", "TypeError"})
    public void documentMethodsWithoutDocument() throws Exception {
        final String html
            = "<div id='d' name='d'>d</div>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "try {\n"
            + "  var i = document.getElementById;\n"
            + "  log(i('d').id);\n"
            + "} catch(e) { logEx(e) }\n"

            + "try {\n"
            + "  var n = document.getElementsByName;\n"
            + "  log(n('d').length);\n"
            + "} catch(e) { logEx(e) }\n"
            + "</script>";
        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"", "", "#0000aa", "#0000aa", "x", "x"})
    public void bgColor() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html>\n"
            + "  <head>\n"
            + "    <script>\n"
            + LOG_TITLE_FUNCTION
            + "      function test() {\n"
            + "        var b = document.getElementById('body');\n"
            + "        log(document.bgColor);\n"
            + "        log(b.bgColor);\n"
            + "        document.bgColor = '#0000aa';\n"
            + "        log(document.bgColor);\n"
            + "        log(b.bgColor);\n"
            + "        document.bgColor = 'x';\n"
            + "        log(document.bgColor);\n"
            + "        log(b.bgColor);\n"
            + "      }\n"
            + "    </script>\n"
            + "  </head>\n"
            + "  <body id='body' onload='test()'>blah</body>\n"
            + "</html>";
        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"[object HTMLCollection]", "4", "red"})
    public void identicalIDs() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html>\n"
            + "  <head>\n"
            + "    <script>\n"
            + LOG_TITLE_FUNCTION
            + "      function test() {\n"
            + "        log(document.all['Item']);\n"
            + "        log(document.all.Item.length);\n"
            + "        if (document.all.Item.length) {\n"
            + "          log(document.all.Item[1].style.color);\n"
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
        loadPageVerifyTitle2(html);
    }

    /**
     * Test that {@code document.forms.form_name} should be evaluated to {@code undefined} if the form has a prefix.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("undefined")
    public void prefix() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "function doTest() {\n"
            + "  log(document.forms.fmLogin);\n"
            + "}\n"
            + "</script></head>\n"
            + "<body onload='doTest()'>\n"
            + "  <s:form name='fmLogin' action='doLogin' method='POST'>\n"
            + "    <s:hidden name='hdUserID'/>\n"
            + "    <s:hidden name='hdPassword'/>\n"
            + "  </s:form>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * Warning: this test works fine in real FF8 when started manually but fails through WebDriver.
     * Warning: opens a modal panel when run through IEDriver which needs to be closed MANUALLY.
     * If not all following test will fail.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = {"0", "IndexSizeError/DOMException"},
            FF = {"1", "[object HTMLBodyElement]"},
            FF_ESR = {"1", "[object HTMLBodyElement]"})
    public void designMode_selectionRange_empty() throws Exception {
        designMode_selectionRange("");
    }

    /**
     * Warning: opens a modal panel when run through IEDriver which needs to be closed MANUALLY.
     * If not all following test will fail.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = {"0", "IndexSizeError/DOMException"},
            FF = {"1", "[object Text]"},
            FF_ESR = {"1", "[object Text]"})
    public void designMode_selectionRange_text() throws Exception {
        designMode_selectionRange("hello");
    }

    private void designMode_selectionRange(final String bodyContent) throws Exception {
        final String html = DOCTYPE_HTML
            + "<html>\n"
            + "<head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "function doTest() {\n"
            + "  try {\n"
            + "    document.designMode = 'on';\n"
            + "    var s = window.getSelection();\n"
            + "    log(s.rangeCount);\n"
            + "    log(s.getRangeAt(0).startContainer);\n"
            + "  } catch(e) {logEx(e); }\n"
            + "}\n"
            + "</script></head>\n"
            + "<body onload='doTest()'>" // no \n here!
            + bodyContent
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    public void all_detection() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    log(!(!document.all));\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("[object HTMLAllCollection]")
    public void all_scriptableToString() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    log(document.all);\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("not defined")
    public void frames() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "function test() {\n"
            + "  if (document.frames) {\n"
            + "    log(document.frames == window.frames);\n"
            + "    log(document.frames.length);\n"
            + "  } else\n"
            + "    log('not defined');\n"
            + "}\n"
            + "</script></head>\n"
            + "<body onload='test();'>\n"
            + "  <iframe src='about:blank' name='foo'></iframe>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * IE allows document.frameName to access a frame window.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"[object Window]", "true"},
            FF_ESR = {"undefined", "false"})
    public void frameAccessByName() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "function test() {\n"
            + "  log(document.foo);\n"
            + "  log(window.frames[0] == document.foo);\n"
            + "}\n"
            + "</script></head>\n"
            + "<body onload='test()'>\n"
            + "  <iframe src='about:blank' name='foo'></iframe>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"0", "0"})
    public void getElementsByName_notFound() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "function doTest() {\n"
            + "  log(document.getElementsByName(null).length);\n"
            + "  log(document.getElementsByName('foo').length);\n"
            + "}\n"
            + "</script></head><body onload='doTest()'>\n"
            + "  <div name='test'></div>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"2", "0", "0"},
            FF = {"0", "0", "0"},
            FF_ESR = {"0", "0", "0"})
    public void getElementsByName_emptyName() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    log(document.getElementsByName('').length);\n"
            + "    log(document.getElementsByName(' ').length);\n"
            + "    log(document.getElementsByName(null).length);\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "  <div name=''></div>\n"
            + "  <div name=''></div>\n"
            + "  <div></div>\n"
            + "  <div></div>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"1", "2", "1", "2", "1", "2", "1", "2", "1", "2", "1", "2"})
    public void getElementsByName_elements() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    try {\n"
            + "      log(document.getElementsByName('form1').length);\n"
            + "    } catch(e) { log('exception:f1') }\n"
            + "    try {\n"
            + "      log(document.getElementsByName('form2').length);\n"
            + "    } catch(e) { log('exception:f2') }\n"
            + "    try {\n"
            + "      log(document.getElementsByName('frame1').length);\n"
            + "    } catch(e) { log('exception:f1') }\n"
            + "    try {\n"
            + "      log(document.getElementsByName('frame2').length);\n"
            + "    } catch(e) { log('exception:f2') }\n"
            + "    try {\n"
            + "      log(document.getElementsByName('input1').length);\n"
            + "    } catch(e) { log('exception:i1') }\n"
            + "    try {\n"
            + "      log(document.getElementsByName('input2').length);\n"
            + "    } catch(e) { log('exception:i2') }\n"
            + "    try {\n"
            + "      log(document.getElementsByName('anchor1').length);\n"
            + "    } catch(e) { log('exception:a1') }\n"
            + "    try {\n"
            + "      log(document.getElementsByName('anchor2').length);\n"
            + "    } catch(e) { log('exception:a2') }\n"
            + "    try {\n"
            + "      log(document.getElementsByName('image1').length);\n"
            + "    } catch(e) { log('exception:i1') }\n"
            + "    try {\n"
            + "      log(document.getElementsByName('image2').length);\n"
            + "    } catch(e) { log('exception:i2') }\n"
            + "    try {\n"
            + "      log(document.getElementsByName('element1').length);\n"
            + "    } catch(e) { log('exception:e1') }\n"
            + "    try {\n"
            + "      log(document.getElementsByName('element2').length);\n"
            + "    } catch(e) { log('exception:e2') }\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "  <form name='form1'></form>\n"
            + "  <form name='form2'></form>\n"
            + "  <form name='form2'></form>\n"
            + "  <iframe name='frame1'></iframe>\n"
            + "  <iframe name='frame2'></iframe>\n"
            + "  <iframe name='frame2'></iframe>\n"
            + "  <input type='text' name='input1' value='1'/>\n"
            + "  <input type='text' name='input2' value='2'/>\n"
            + "  <input type='text' name='input2' value='3'/>\n"
            + "  <a name='anchor1'></a>\n"
            + "  <a name='anchor2'></a>\n"
            + "  <a name='anchor2'></a>\n"
            + "  <img name='image1'>\n"
            + "  <img name='image2'>\n"
            + "  <img name='image2'>\n"
            + "  <div name='element1'></table>\n"
            + "  <div name='element2'></div>\n"
            + "  <div name='element2'></div>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"1", "2"})
    public void getElementsByName_frame() throws Exception {
        final String html = "<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01 Frameset//EN\""
            + "\"http://www.w3.org/TR/html4/frameset.dtd\">\n"
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    try {\n"
            + "      log(document.getElementsByName('frame1').length);\n"
            + "    } catch(e) { log('exception:f1') }\n"
            + "    try {\n"
            + "      log(document.getElementsByName('frame2').length);\n"
            + "    } catch(e) { log('exception:f2') }\n"
            + "  }\n"
            + "</script></head>\n"
            + "<frameset onload='test()'>\n"
            + "  <frame src='" + URL_SECOND + "' name='frame1'>\n"
            + "  <frame src='" + URL_SECOND + "' name='frame2'>\n"
            + "  <frame src='" + URL_SECOND + "' name='frame2'>\n"
            + "</frameset>\n"
            + "</html>";

        final String frame = DOCTYPE_HTML
                + "<html><head><title>frame</title></head><body></body></html>";
        getMockWebConnection().setDefaultResponse(frame);

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "9"})
    public void getElementsByName_changedAfterGet() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            // 1
            + "    var collection = document.getElementsByName('image1');\n"
            + "    log(collection.length);\n"

            // 2
            + "    var newImage1 = document.createElement('img');\n"
            + "    newImage1.name = 'image1';\n"
            + "    document.getElementById('outer1').appendChild(newImage1);\n"
            + "    log(collection.length);\n"

            // 3
            + "    var newImage2 = document.createElement('img');\n"
            + "    newImage2.name = 'image1';\n"
            + "    document.getElementById('outer2').insertBefore(newImage2, null);\n"
            + "    log(collection.length);\n"

            // 4
            + "    var newImage3 = document.createElement('img');\n"
            + "    newImage3.name = 'image1';\n"
            + "    document.getElementById('outer3').replaceChild(newImage3, document.getElementById('inner3'));\n"
            + "    log(collection.length);\n"

            // 5
            + "    document.getElementById('outer4').outerHTML = '<img name=\"image1\">';\n"
            + "    log(collection.length);\n"

            // 6
            + "    document.getElementById('outer5').innerHTML = '<img name=\"image1\">';\n"
            + "    log(collection.length);\n"

            // 7
            + "    document.getElementById('outer6').insertAdjacentHTML('beforeend', '<img name=\"image1\">');\n"
            + "    log(collection.length);\n"

            // 8
            + "    document.getElementById('image3').setAttribute('name', 'image1');\n"
            + "    log(collection.length);\n"

            // 9
            + "    var newAttr = document.createAttribute('name');\n"
            + "    newAttr.nodeValue = 'image1';\n"
            + "    document.getElementById('image4').setAttributeNode(newAttr);\n"
            + "    log(collection.length);\n"

            // 10
            + "    try {\n"
            + "      document.getElementById('image5').setAttributeNS(null, 'name', 'image1');\n"
            + "      log(collection.length);\n"
            + "    } catch(e) { log('exception:setAttributeNS') }\n"

            // 9
            + "    document.getElementById('outer1').removeChild(newImage1);\n"
            + "    log(collection.length);\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "  <img name='image1'>\n"
            + "  <div id='outer1'></div>\n"
            + "  <div id='outer2'></div>\n"
            + "  <div id='outer3'><div id='inner3'></div></div>\n"
            + "  <div id='outer4'></div>\n"
            + "  <div id='outer5'></div>\n"
            + "  <div id='outer6'></div>\n"
            + "  <img id='image2'>\n"
            + "  <img id='image3'>\n"
            + "  <img id='image4'>\n"
            + "  <img id='image5'>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * Contains the cases of test {@link #getElementsByName_changedAfterGet()} that are not yet implemented.<br>
     * If a case gets implemented, move it to {@link #getElementsByName_changedAfterGet()}.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"1", "2"})
    public void getElementsByName_changedAfterGet2() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var collection = document.getElementsByName('image1');\n"
            + "    log(collection.length);\n"
            + "    document.getElementById('image2').name = 'image1';\n"
            + "    log(collection.length);\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "  <img name='image1'>\n"
            + "  <img id='image2'>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "9"})
    public void getElementsByName_changedAfterGet_nested() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            // 1
            + "    var collection = document.getElementsByName('image1');\n"
            + "    log(collection.length);\n"

            // 2
            + "    var newImage1 = document.createElement('img');\n"
            + "    newImage1.name = 'image1';\n"
            + "    document.getElementById('outer1').appendChild(newImage1);\n"
            + "    log(collection.length);\n"

            // 3
            + "    var newImage2 = document.createElement('img');\n"
            + "    newImage2.name = 'image1';\n"
            + "    document.getElementById('outer2').insertBefore(newImage2, null);\n"
            + "    log(collection.length);\n"

            // 4
            + "    var newImage3 = document.createElement('img');\n"
            + "    newImage3.name = 'image1';\n"
            + "    document.getElementById('outer3').replaceChild(newImage3, document.getElementById('inner3'));\n"
            + "    log(collection.length);\n"

            // 5
            + "    document.getElementById('outer4').outerHTML = '<img name=\"image1\">';\n"
            + "    log(collection.length);\n"

            // 6
            + "    document.getElementById('outer5').innerHTML = '<img name=\"image1\">';\n"
            + "    log(collection.length);\n"

            // 7
            + "    document.getElementById('outer6').insertAdjacentHTML('beforeend', '<img name=\"image1\">');\n"
            + "    log(collection.length);\n"

            // 8
            + "    document.getElementById('image3').setAttribute('name', 'image1');\n"
            + "    log(collection.length);\n"

            // 9
            + "    var newAttr = document.createAttribute('name');\n"
            + "    newAttr.nodeValue = 'image1';\n"
            + "    document.getElementById('image4').setAttributeNode(newAttr);\n"
            + "    log(collection.length);\n"

            // 10
            + "    try {\n"
            + "      document.getElementById('image5').setAttributeNS(null, 'name', 'image1');\n"
            + "      log(collection.length);\n"
            + "    } catch(e) { log('exception:setAttributeNS') }\n"

            // 9
            + "    document.getElementById('outer1').removeChild(newImage1);\n"
            + "    log(collection.length);\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "  <div>\n"
            + "    <img name='image1'>\n"
            + "    <div id='outer1'></div>\n"
            + "    <div id='outer2'></div>\n"
            + "    <div id='outer3'><div id='inner3'></div></div>\n"
            + "    <div id='outer4'></div>\n"
            + "    <div id='outer5'></div>\n"
            + "    <div id='outer6'></div>\n"
            + "    <img id='image2'>\n"
            + "    <img id='image3'>\n"
            + "    <img id='image4'>\n"
            + "    <img id='image5'>\n"
            + "  </div>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * Contains the cases of test {@link #getElementsByName_changedAfterGet_nested()} that are not yet implemented.<br>
     * If a case gets implemented, move it to {@link #getElementsByName_changedAfterGet_nested()}.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"1", "2"})
    public void getElementsByName_changedAfterGet_nested2() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var collection = document.getElementsByName('image1');\n"
            + "    log(collection.length);\n"
            + "    document.getElementById('image2').name = 'image1';\n"
            + "    log(collection.length);\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "  <div>\n"
            + "    <img name='image1'>\n"
            + "    <img id='image2'>\n"
            + "  </div>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * Regression test for a bug introduced by the document proxy and detected by the Dojo JavaScript library tests.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("true")
    public void equalityViaDifferentPaths() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><body>\n"
            + "<script>"
            + LOG_TITLE_FUNCTION
            + "log(document.body.parentNode.parentNode === document)\n"
            + "</script>\n"
            + "</body></html>";
        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("TypeError")
    public void getBoxObjectFor() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "function doTest() {\n"
            + "  var e = document.getElementById('log');\n"
            + "  try {\n"
            + "    var a = document.getBoxObjectFor(e);\n"
            + "    log(a);\n"
            + "    log(a === document.getBoxObjectFor(e));\n"
            + "    log(a.screenX > 0);\n"
            + "    log(a.screenY > 0);\n"
            + "  } catch(e) { logEx(e) }\n"
            + "}\n"
            + "</script></head><body onload='doTest()'>\n"
            + "<div id='log'></div>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"32 commands supported", "not supported: foo, 123"})
    @BuggyWebDriver({"31 commands supported", "not supported: Paste, foo, 123"})
    public void queryCommandSupported_common() throws Exception {
        final String[] commands = {"BackColor", "Bold",
            "Copy", "CreateLink", "Cut", "Delete",
            "FontName", "FontSize", "ForeColor", "FormatBlock",
            "Indent", "InsertHorizontalRule", "InsertImage", "InsertOrderedList",
            "InsertParagraph", "InsertUnorderedList", "Italic",
            "JustifyCenter", "JustifyFull", "JustifyLeft", "JustifyRight",
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
    @Alerts(DEFAULT = {"3 commands supported", "not supported: 2D-Position, AbsolutePosition, "
                    + "BlockDirLTR, BlockDirRTL, BrowseMode, ClearAuthenticationCache, CreateBookmark, "
                    + "DirLTR, DirRTL, EditMode, InlineDirLTR, InlineDirRTL, InsertButton, InsertFieldset, "
                    + "InsertIFrame, InsertInputButton, InsertInputCheckbox, InsertInputFileUpload, "
                    + "InsertInputHidden, InsertInputImage, InsertInputPassword, InsertInputRadio, "
                    + "InsertInputReset, InsertInputSubmit, InsertInputText, InsertMarquee, InsertSelectDropdown, "
                    + "InsertSelectListbox, InsertTextArea, LiveResize, MultipleSelection, "
                    + "Open, OverWrite, PlayImage, Refresh, RemoveParaFormat, SaveAs, SizeToControl, "
                    + "SizeToControlHeight, SizeToControlWidth, Stop, StopImage, UnBookmark"},
            FF = "0 commands supported",
            FF_ESR = "0 commands supported")
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
        final String jsCommandArray = "['" + String.join("', '", commands) + "']";
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "function doTest() {\n"
            + "  var cmds = " + jsCommandArray + ";\n"
            + "  var nbSupported = 0;\n"
            + "  var cmdsNotSupported = [];\n"
            + "  try {\n"
            + "    for (var i = 0; i < cmds.length; i++) {\n"
            + "      var cmd = cmds[i];\n"
            + "      var b = document.queryCommandSupported(cmd);\n"
            + "      if (b)\n"
            + "        nbSupported++;\n"
            + "      else\n"
            + "        cmdsNotSupported[cmdsNotSupported.length] = cmd;\n"
            + "    }\n"
            + "  } catch(e) { logEx(e); }\n"
            + "  log(nbSupported + ' commands supported');\n"
            + "  if (nbSupported != 0 && cmdsNotSupported.length > 0)\n"
            + "    log('not supported: ' + cmdsNotSupported.join(', '));\n"
            + "}\n"
            + "</script></head><body onload='doTest()'>\n"
            + "<div id='log'></div>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"3", "div1"})
    public void querySelectorAll() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head>\n"
            + "<style>\n"
            + "  .red   {color:#FF0000;}\n"
            + "  .green {color:#00FF00;}\n"
            + "  .blue  {color:#0000FF;}\n"
            + "</style>\n"
            + "<script>\n"
            + "function test() {\n"
            + LOG_TITLE_FUNCTION
            + "  var redTags = document.querySelectorAll('.green,.red');\n"
            + "  log(redTags.length);\n"
            + "  log(redTags.item(0).id);\n"
            + "}\n"
            + "</script></head><body onload='test()'>\n"
            + "  <div id='div1' class='red'>First</div>\n"
            + "  <div id='div2' class='red'>Second</div>\n"
            + "  <div id='div3' class='green'>Third</div>\n"
            + "  <div id='div4' class='blue'>Fourth</div>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("[object NodeList]")
    public void querySelectorAllType() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "function test() {\n"
            + "  log(document.querySelectorAll('html'));\n"
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
    @Alerts("SyntaxError/DOMException")
    public void querySelectorAll_badSelector() throws Exception {
        for (final String selector : JQUERY_CUSTOM_SELECTORS) {
            doTestQuerySelectorAll_badSelector(selector);
        }
    }

    private void doTestQuerySelectorAll_badSelector(final String selector) throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><body><script>\n"
            + LOG_TITLE_FUNCTION
            + "try {\n"
            + "  document.querySelectorAll('" + selector + "');\n"
            + "  log('working');\n"
            + "} catch(e) { logEx(e); }\n"
            + "</script></body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("SyntaxError/DOMException")
    public void querySelector_badSelector() throws Exception {
        for (final String selector : JQUERY_CUSTOM_SELECTORS) {
            doTestQuerySelector_badSelector(selector);
        }
    }

    private void doTestQuerySelector_badSelector(final String selector) throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><body><script>\n"
            + LOG_TITLE_FUNCTION
            + "try {\n"
            + "  document.querySelector('" + selector + "');\n"
            + "  log('working: " + selector + "');\n"
            + "} catch(e) { logEx(e); }\n"
            + "</script></body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"3", "div1"})
    public void querySelectorAll_quirks() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html>\n"
            + "<head>\n"
            + "<meta http-equiv='X-UA-Compatible' content='IE=7' />\n"
            + "<style>\n"
            + "  .red   {color:#FF0000;}\n"
            + "  .green {color:#00FF00;}\n"
            + "  .blue  {color:#0000FF;}\n"
            + "</style>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "function test() {\n"
            + "  if(document.querySelectorAll) {\n"
            + "    var redTags = document.querySelectorAll('.green,.red');\n"
            + "    log(redTags.length);\n"
            + "    log(redTags.item(0).id);\n"
            + "  }\n"
            + "  else\n"
            + "    log('undefined');\n"
            + "}\n"
            + "</script></head>\n"
            + "<body onload='test()'>\n"
            + "  <div id='div1' class='red'>First</div>\n"
            + "  <div id='div2' class='red'>Second</div>\n"
            + "  <div id='div3' class='green'>Third</div>\n"
            + "  <div id='div4' class='blue'>Fourth</div>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("3")
    public void querySelectorAll_implicitAttribute() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "function test() {\n"
            + "  var result = document.querySelectorAll('[disabled]');\n"
            + "  log(result.length);\n"
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

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"div1", "null"})
    public void querySelector() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head>\n"
            + "<style>\n"
            + "  .red   {color:#FF0000;}\n"
            + "  .green {color:#00FF00;}\n"
            + "  .blue  {color:#0000FF;}\n"
            + "</style>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "function test() {\n"
            + "  log(document.querySelector('.green,.red').id);\n"
            + "  log(document.querySelector('.orange'));\n"
            + "}\n"
            + "</script></head><body onload='test()'>\n"
            + "  <div id='div1' class='red'>First</div>\n"
            + "  <div id='div2' class='red'>Second</div>\n"
            + "  <div id='div3' class='green'>Third</div>\n"
            + "  <div id='div4' class='blue'>Fourth</div>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"1", "0"})
    public void getElementsByTagName2() throws Exception {
        final String html = "<html xmlns:ns1='http://example.com'>\n"
            + "<head>\n"
            + "  <script>\n"
            + LOG_TITLE_FUNCTION
            + "    function test() {\n"
            + "      log(document.getElementsByTagName('ns1:ele').length);\n"
            + "      log(document.getElementsByTagName('ele').length);\n"
            + "    }\n"
            + "  </script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "  <ns1:ele>&nbsp;</ns1:ele>\n"
            + "</body>\n"
            + "</html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"1", "0"})
    public void getElementsByTagName3() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html>\n"
            + "<head>\n"
            + "  <script>\n"
            + LOG_TITLE_FUNCTION
            + "    function test() {\n"
            + "      log(document.getElementsByTagName('ns1:ele').length);\n"
            + "      log(document.getElementsByTagName('ele').length);\n"
            + "    }\n"
            + "  </script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "  <ns1:ele>&nbsp;</ns1:ele>\n"
            + "</body>\n"
            + "</html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * Even if clear() does nothing, it was missing until HtmlUnit-2.8 and this test was failing.
     * @throws Exception if the test fails
     */
    @Test
    public void clear() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head>\n"
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
    @Alerts({"true", "", "foo=bar", "foo=hello world"})
    public void cookie_write_cookiesEnabled() throws Exception {
        final String html = DOCTYPE_HTML
              + "<html><head><script>\n"
              + LOG_TITLE_FUNCTION
              + "  log(navigator.cookieEnabled);\n"
              + "  log(document.cookie);\n"
              + "  document.cookie = 'foo=bar';\n"
              + "  log(document.cookie);\n"
              + "  document.cookie = 'foo=hello world';\n"
              + "  log(document.cookie);\n"
              + "</script>\n"
              + "</head>\n"
              + "<body>abc</body>\n"
              + "</html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = {"", "a", "a", "b", "b"},
            FF_ESR = {"", "a", "", "b", ""})
    public void cookie_write2() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html>\n"
            + "  <head>\n"
            + "    <script>\n"
            + LOG_TITLE_FUNCTION
            + "      log(document.cookie);\n"
            + "      document.cookie = 'a';\n"
            + "      log(document.cookie);\n"
            + "      document.cookie = '';\n"
            + "      log(document.cookie);\n"
            + "      document.cookie = 'b';\n"
            + "      log(document.cookie);\n"
            + "      document.cookie = ' ';\n"
            + "      log(document.cookie);\n"
            + "    </script>\n"
            + "  </head>\n"
            + "  <body>abc</body>\n"
            + "</html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"", "a", "b"})
    public void cookie_write_valueOnly() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html>\n"
            + "  <head>\n"
            + "    <script>\n"
            + LOG_TITLE_FUNCTION
            + "      log(document.cookie);\n"
            + "      document.cookie = 'a';\n"
            + "      log(document.cookie);\n"
            + "      document.cookie = '=b';\n"
            + "      log(document.cookie);\n"
            + "    </script>\n"
            + "  </head>\n"
            + "  <body>abc</body>\n"
            + "</html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * Regression test for bug 3030247: expired cookie was saved.
     * @see <a href="http://sourceforge.net/tracker/?func=detail&aid=3030247&group_id=47038&atid=448266">1139</a>
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"", "test2=1", ""})
    public void writeCookieExpired() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><body>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "log(document.cookie);\n"
            + "document.cookie = 'test2=1';\n"
            + "log(document.cookie);\n"
            + "document.cookie = 'test2=;expires=Fri, 02-Jan-1970 00:00:00 GMT';\n"
            + "log(document.cookie);\n"
            + "</script></body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * Only IE accepts more than the tag name.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("InvalidCharacterError/DOMException")
    public void createElement_notOnlyTagName() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><body>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "try {\n"
            + "  var t = document.createElement('<input name=x>');\n"
            + "  log(t.tagName);\n"
            + "} catch(e) {\n"
            + "  logEx(e);\n"
            + "}\n"
            + "</script>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"myattr", ""})
    public void createAttributeNameValue() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html>\n"
            + "<head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var node = document.createAttribute('myAttr');\n"
            + "    log(node.name);\n"
            + "    log(node.value);\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "  <div id='tester'></div>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
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
    @Alerts("null")
    public void getElementById_quirks() throws Exception {
        getElementById_strict(false);
    }

    private void getElementById_strict(final boolean xhtml) throws Exception {
        final String header = xhtml ? "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" "
                + "\"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">\n" : "";
        final String html = header + "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    log(document.getElementById('myId'));\n"
            + "  }\n"
            + "</script>\n"
            + "</head><body onload=test()>\n"
            + "  <a name='myId'/>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("null")
    public void getElementById_caseSensitivity() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html>\n"
            + "<head>\n"
            + "  <script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    log(document.getElementById('MYDIV'));\n"
            + "  }\n"
            + "  </script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "<div id='myDiv'>\n"
            + "  <div></div>\n"
            + "</div>\n"
            + "</body>\n"
            + "</html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"null", "null", "null"})
    public void getElementById_emptyParams() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html>\n"
            + "<head>\n"
            + "  <script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    log(document.getElementById(''));\n"
            + "    log(document.getElementById(undefined));\n"
            + "    log(document.getElementById(null));\n"
            + "  }\n"
            + "  </script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "<div id='myDiv'>\n"
            + "  <div></div>\n"
            + "</div>\n"
            + "</body>\n"
            + "</html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("[object HTMLHeadElement]")
    public void head() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><body>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  log(document.head);\n"
            + "</script>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"", "", "#0000aa", "#0000aa", "x", "x"})
    public void alinkColor() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html>\n"
            + "  <head>\n"
            + "    <script>\n"
            + LOG_TITLE_FUNCTION
            + "      function test() {\n"
            + "        var b = document.getElementById('body');\n"
            + "        log(document.alinkColor);\n"
            + "        log(b.aLink);\n"
            + "        document.alinkColor = '#0000aa';\n"
            + "        log(document.alinkColor);\n"
            + "        log(b.aLink);\n"
            + "        document.alinkColor = 'x';\n"
            + "        log(document.alinkColor);\n"
            + "        log(b.aLink);\n"
            + "      }\n"
            + "    </script>\n"
            + "  </head>\n"
            + "  <body id='body' onload='test()'>blah</body>\n"
            + "</html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"", "", "#0000aa", "#0000aa", "x", "x"})
    public void linkColor() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html>\n"
            + "  <head>\n"
            + "    <script>\n"
            + LOG_TITLE_FUNCTION
            + "      function test() {\n"
            + "        var b = document.getElementById('body');\n"
            + "        log(document.linkColor);\n"
            + "        log(b.link);\n"
            + "        document.linkColor = '#0000aa';\n"
            + "        log(document.linkColor);\n"
            + "        log(b.link);\n"
            + "        document.linkColor = 'x';\n"
            + "        log(document.linkColor);\n"
            + "        log(b.link);\n"
            + "      }\n"
            + "    </script>\n"
            + "  </head>\n"
            + "  <body id='body' onload='test()'>blah</body>\n"
            + "</html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"", "", "#0000aa", "#0000aa", "x", "x"})
    public void vlinkColor() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html>\n"
            + "  <head>\n"
            + "    <script>\n"
            + LOG_TITLE_FUNCTION
            + "      function test() {\n"
            + "        var b = document.getElementById('body');\n"
            + "        log(document.vlinkColor);\n"
            + "        log(b.vLink);\n"
            + "        document.vlinkColor = '#0000aa';\n"
            + "        log(document.vlinkColor);\n"
            + "        log(b.vLink);\n"
            + "        document.vlinkColor = 'x';\n"
            + "        log(document.vlinkColor);\n"
            + "        log(b.vLink);\n"
            + "      }\n"
            + "    </script>\n"
            + "  </head>\n"
            + "  <body id='body' onload='test()'>blah</body>\n"
            + "</html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"", "", "#0000aa", "#0000aa", "x", "x"})
    public void fgColor() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html>\n"
            + "  <head>\n"
            + "    <script>\n"
            + LOG_TITLE_FUNCTION
            + "      function test() {\n"
            + "        var b = document.getElementById('body');\n"
            + "        log(document.fgColor);\n"
            + "        log(b.text);\n"
            + "        document.fgColor = '#0000aa';\n"
            + "        log(document.fgColor);\n"
            + "        log(b.text);\n"
            + "        document.fgColor = 'x';\n"
            + "        log(document.fgColor);\n"
            + "        log(b.text);\n"
            + "      }\n"
            + "    </script>\n"
            + "  </head>\n"
            + "  <body id='body' onload='test()'>blah</body>\n"
            + "</html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"", "true"})
    public void getSelection() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html>\n"
            + "  <head>\n"
            + "    <script>\n"
            + LOG_TITLE_FUNCTION
            + "      function test() {\n"
            + "        if (document.getSelection) {\n"
            + "          log(document.getSelection());\n"
            + "          log(document.getSelection() === window.getSelection());\n"
            + "        }\n"
            + "      }\n"
            + "    </script>\n"
            + "  </head>\n"
            + "  <body id='body' onload='test()'>blah</body>\n"
            + "</html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"true", "undefined", "false"})
    public void document_xxx_formAccess() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html>\n"
            + "<head>\n"
            + "  <script>\n"
            + LOG_TITLE_FUNCTION
            + "    function test() {\n"
            + "      log(document.foo == document.forms.foo);\n"
            + "      log(document.blah);\n"
            + "      log(document.blah == document.forms.foo);\n"
            + "    }\n"
            + "  </script>\n"
            + "</head><body onload='test()'>\n"
            + "  <div id='foo'>the div 1</div>\n"
            + "  <form name='foo' id='blah'>\n"
            + "    <input name='foo'>\n"
            + "  </form>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"windows-1252", "windows-1252", "windows-1252", "undefined"})
    public void encoding() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html>\n"
            + "<head>\n"
            + "  <script>\n"
            + LOG_TITLE_FUNCTION
            + "    function test() {\n"
            + "      log(document.inputEncoding);\n"
            + "      log(document.characterSet);\n"
            + "      log(document.charset);\n"
            + "      log(document.defaultCharset);\n"
            + "    }\n"
            + "  </script>\n"
            + "</head><body onload='test()'>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"windows-1252", "windows-1252", "windows-1252", "undefined"})
    public void encoding2() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html>\n"
            + "<head>\n"
            + "  <meta http-equiv='Content-Type' content='text/html; charset=ISO-8859-1'>\n"
            + "  <script>\n"
            + LOG_TITLE_FUNCTION
            + "    function test() {\n"
            + "      log(document.inputEncoding);\n"
            + "      log(document.characterSet);\n"
            + "      log(document.charset);\n"
            + "      log(document.defaultCharset);\n"
            + "    }\n"
            + "  </script>\n"
            + "</head><body onload='test()'>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"windows-1252", "windows-1252", "windows-1252", "undefined"})
    public void encoding3() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html>\n"
            + "<head>\n"
            + "  <meta http-equiv='Content-Type' content='text/html; charset=iso-8859-1'>\n"
            + "  <script>\n"
            + LOG_TITLE_FUNCTION
            + "    function test() {\n"
            + "      log(document.inputEncoding);\n"
            + "      log(document.characterSet);\n"
            + "      log(document.charset);\n"
            + "      log(document.defaultCharset);\n"
            + "    }\n"
            + "  </script>\n"
            + "</head><body onload='test()'>\n"
            + "</body></html>";

        final String[] expectedAlerts = getExpectedAlerts();
        final WebDriver driver = loadPage2(html, URL_FIRST, MimeType.TEXT_HTML, ISO_8859_1);
        verifyTitle2(driver, expectedAlerts);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"UTF-8", "UTF-8", "UTF-8", "undefined"})
    public void encoding4() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html>\n"
            + "<head>\n"
            + "  <meta http-equiv='Content-Type' content='text/html; charset=iso-8859-1'>\n"
            + "  <script>\n"
            + LOG_TITLE_FUNCTION
            + "    function test() {\n"
            + "      log(document.inputEncoding);\n"
            + "      log(document.characterSet);\n"
            + "      log(document.charset);\n"
            + "      log(document.defaultCharset);\n"
            + "    }\n"
            + "  </script>\n"
            + "</head><body onload='test()'>\n"
            + "</body></html>";

        final String[] expectedAlerts = getExpectedAlerts();
        final WebDriver driver = loadPage2(html, URL_FIRST, "text/html;charset=UTF-8", ISO_8859_1);
        verifyTitle2(driver, expectedAlerts);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"UTF-8", "UTF-8", "UTF-8", "undefined"})
    public void encoding5() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html>\n"
            + "<head>\n"
            + "  <meta http-equiv='Content-Type' content='text/html; charset=iso-8859-1'>\n"
            + "  <script>\n"
            + LOG_TITLE_FUNCTION
            + "    function test() {\n"
            + "      log(document.inputEncoding);\n"
            + "      log(document.characterSet);\n"
            + "      log(document.charset);\n"
            + "      log(document.defaultCharset);\n"
            + "    }\n"
            + "  </script>\n"
            + "</head><body onload='test()'>\n"
            + "</body></html>";

        final String[] expectedAlerts = getExpectedAlerts();
        final WebDriver driver = loadPage2(html, URL_FIRST, "text/html;charset=utf-8", ISO_8859_1);
        verifyTitle2(driver, expectedAlerts);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"UTF-8", "UTF-8", "UTF-8", "undefined"})
    public void encoding6() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html>\n"
            + "<head>\n"
            + "  <meta charset='UTF-8'>\n"
            + "  <script>\n"
            + LOG_TITLE_FUNCTION
            + "    function test() {\n"
            + "      log(document.inputEncoding);\n"
            + "      log(document.characterSet);\n"
            + "      log(document.charset);\n"
            + "      log(document.defaultCharset);\n"
            + "    }\n"
            + "  </script>\n"
            + "</head><body onload='test()'>\n"
            + "  <a id='myId' href='test?è=è'>test</a>\n"
            + "</body></html>";

        final String[] expectedAlerts = getExpectedAlerts();
        final WebDriver driver = loadPage2(html, URL_FIRST, MimeType.TEXT_HTML, UTF_8);
        verifyTitle2(driver, expectedAlerts);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("?%C3%A8=%C3%A8")
    public void encoding7() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html>\n"
            + "<head>\n"
            + "<meta charset='UTF-8'>\n"
            + "</head><body>\n"
            + "  <a id='myId' href='test?\u00E8=\u00E8'>test</a>\n"
            + "</body></html>";
        getMockWebConnection().setDefaultResponse("Error: not found", 404, "Not Found", MimeType.TEXT_HTML);

        final WebDriver driver = loadPage2(html, URL_FIRST, MimeType.TEXT_HTML, UTF_8);
        driver.findElement(By.id("myId")).click();
        String actualQuery = driver.getCurrentUrl();
        actualQuery = actualQuery.substring(actualQuery.indexOf('?'));
        assertTrue(actualQuery.endsWith(getExpectedAlerts()[0]));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"undefined", "BackCompat", "function", "function"})
    public void documentMode() throws Exception {
        documentMode("", "");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"undefined", "CSS1Compat", "function", "function"})
    public void documentMode_doctypeStrict() throws Exception {
        documentMode(DOCTYPE_HTML, "");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"undefined", "BackCompat", "function", "function"})
    public void documentMode_doctypeTransitional() throws Exception {
        documentMode("<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.0 Transitional//EN\""
                + " \"http://www.w3.org/TR/html4/loose.dtd\">\n", "");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"undefined", "CSS1Compat", "function", "function"})
    public void documentMode_doctypeHTML5() throws Exception {
        documentMode("<!DOCTYPE html>\n", "");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"undefined", "BackCompat", "function", "function"})
    public void documentMode_metaIE5() throws Exception {
        documentMode("", "  <meta http-equiv='X-UA-Compatible' content='IE=5'>\n");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"undefined", "BackCompat", "function", "function"})
    public void documentMode_metaIE8() throws Exception {
        documentMode("", "  <meta http-equiv='X-UA-Compatible' content='IE=8'>\n");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"undefined", "CSS1Compat", "function", "function"})
    public void documentMode_metaIE8_doctypeStrict() throws Exception {
        documentMode(DOCTYPE_HTML, "  <meta http-equiv='X-UA-Compatible' content='IE=8'>\n");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"undefined", "BackCompat", "function", "function"})
    public void documentMode_metaEmulateIE8() throws Exception {
        documentMode("", "  <meta http-equiv='X-UA-Compatible' content='IE=Emulate8'>\n");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"undefined", "CSS1Compat", "function", "function"})
    public void documentMode_metaEmulateIE8_doctypeStrict() throws Exception {
        documentMode(DOCTYPE_HTML,
                "  <meta http-equiv='X-UA-Compatible' content='IE=Emulate8'>\n");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"undefined", "BackCompat", "function", "function"})
    public void documentMode_metaIE9() throws Exception {
        documentMode("", "  <meta http-equiv='X-UA-Compatible' content='IE=9'>\n");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"undefined", "CSS1Compat", "function", "function"})
    public void documentMode_metaIE9_doctypeStrict() throws Exception {
        documentMode(DOCTYPE_HTML,
                "  <meta http-equiv='X-UA-Compatible' content='IE=9'>\n");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"undefined", "BackCompat", "function", "function"})
    public void documentMode_metaIEEdge() throws Exception {
        documentMode("", "  <meta http-equiv='X-UA-Compatible' content='IE=edge'>\n");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"undefined", "CSS1Compat", "function", "function"})
    public void documentMode_metaIEEdge_doctypeStrict() throws Exception {
        documentMode(DOCTYPE_HTML,
                "  <meta http-equiv='X-UA-Compatible' content='IE=edge'>\n");
    }

    private void documentMode(final String doctype, final String meta) throws Exception {
        final String html = doctype
            + "<html>\n"
            + "<head>\n"
            + meta
            + "  <script>\n"
            + LOG_TITLE_FUNCTION
            + "    function test() {\n"
            + "      log(document.documentMode);\n"
            + "      log(document.compatMode);\n"
            + "      log(typeof document.querySelectorAll);\n"
            + "      log(typeof document.getElementById('myDiv').querySelector);\n"
            + "    }\n"
            + "  </script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "  <div id='myDiv'></div>\n"
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
            + "  log('foo' == document);\n"
            + "</script>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }


    /**
     * Simple test that calls setCapture.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("undefined")
    public void setCapture() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    try {\n"
            + "      log(document.setCapture);\n"
            + "    } catch(e) { logEx(e); }\n"
            + "  }\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "  <div id='myDiv'></div>\n"
            + "</body></html>";
        loadPageVerifyTitle2(html);
    }

    /**
     * Simple test that calls releaseCapture.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"undefined", "releaseCapture available"},
            CHROME = "TypeError",
            EDGE = "TypeError")
    public void releaseCapture() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    try {\n"
            + "      log(document.releaseCapture());\n"
            + "      log('releaseCapture available');\n"
            + "    } catch(e) { logEx(e); }\n"
            + "  }\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "  <div id='myDiv'></div>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(CHROME = {"[object HTMLDocument]", "function HTMLDocument() { [native code] }"},
            EDGE = {"[object HTMLDocument]", "function HTMLDocument() { [native code] }"},
            FF = {"[object HTMLDocument]", "function HTMLDocument() { [native code] }"},
            FF_ESR = {"[object HTMLDocument]", "function HTMLDocument() { [native code] }"})
    public void type() throws Exception {
        final String html = ""
            + "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    try {\n"
            + "      log(document);\n"
            + "      log(HTMLDocument);\n"
            + "    } catch(e) { logEx(e); }\n"
            + "  }\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "  <div id='myDiv'></div>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("§§URL§§")
    public void baseURI_noBaseTag() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html>\n"
                + "<body>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "  log(document.baseURI);\n"
                + "</script>\n"
                + "</body></html>";

        expandExpectedAlertsVariables(URL_FIRST);
        final WebDriver driver = loadPageVerifyTitle2(html);
        if (driver instanceof HtmlUnitDriver && !"undefined".equals(getExpectedAlerts()[0])) {
            final HtmlPage page = (HtmlPage) getEnclosedPage();
            assertEquals(getExpectedAlerts()[0], page.getBaseURL().toString());
        }
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("§§URL§§details/abc")
    public void baseURI_noBaseTag_urlPath() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html>\n"
                + "<body>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "  log(document.baseURI);\n"
                + "</script>\n"
                + "</body></html>";

        expandExpectedAlertsVariables(URL_FIRST);
        final URL url = new URL(URL_FIRST.toString() + "details/abc");
        final WebDriver driver = loadPage2(html, url);
        verifyTitle2(driver, getExpectedAlerts());
        if (driver instanceof HtmlUnitDriver && !"undefined".equals(getExpectedAlerts()[0])) {
            final HtmlPage page = (HtmlPage) getEnclosedPage();
            assertEquals(getExpectedAlerts()[0], page.getBaseURL().toString());
        }
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("§§URL§§?x=y&z=zz")
    public void baseURI_noBaseTag_urlParams() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html>\n"
                + "<body>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "  log(document.baseURI);\n"
                + "</script>\n"
                + "</body></html>";

        expandExpectedAlertsVariables(URL_FIRST);
        final URL url = new URL(URL_FIRST.toString() + "?x=y&z=zz");
        final WebDriver driver = loadPage2(html, url);
        verifyTitle2(driver, getExpectedAlerts());
        if (driver instanceof HtmlUnitDriver && !"undefined".equals(getExpectedAlerts()[0])) {
            final HtmlPage page = (HtmlPage) getEnclosedPage();
            assertEquals(getExpectedAlerts()[0], page.getBaseURL().toString());
        }
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("§§URL§§details/abc;jsessionid=42?x=y&z=zz")
    public void baseURI_noBaseTag_urlPathAndParams() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html>\n"
                + "<body>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "  log(document.baseURI);\n"
                + "</script>\n"
                + "</body></html>";

        final URL url = new URL(URL_FIRST.toString() + "details/abc;jsessionid=42?x=y&z=zz");
        expandExpectedAlertsVariables(URL_FIRST);
        final WebDriver driver = loadPage2(html, url);
        verifyTitle2(driver, getExpectedAlerts());
        if (driver instanceof HtmlUnitDriver && !"undefined".equals(getExpectedAlerts()[0])) {
            final HtmlPage page = (HtmlPage) getEnclosedPage();
            assertEquals(getExpectedAlerts()[0], page.getBaseURL().toString());
        }
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("http://myotherwebsite.com/foo")
    public void baseURI_withBaseTag() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html>\n"
                + "<head>\n"
                + "  <base href='http://myotherwebsite.com/foo'>\n"
                + "</head>\n"
                + "<body>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "  log(document.baseURI);\n"
                + "</script></body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("http://myotherwebsite.com/foo")
    public void baseURI_withBaseTagInBody() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html>\n"
                + "<body>\n"
                + "<base href='http://myotherwebsite.com/foo'>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "  log(document.baseURI);\n"
                + "</script>\n"
                + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("§§URL§§img/")
    public void baseURI_withBaseTag_absolutePath() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html>\n"
                + "<head>\n"
                + "  <base href='/img/'>\n"
                + "</head>\n"
                + "<body>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "  log(document.baseURI);\n"
                + "</script>\n"
                + "</body></html>";

        expandExpectedAlertsVariables(URL_FIRST);
        loadPage2(html, new URL("http://localhost:" + PORT + "/path/to/page.html"));
        verifyTitle2(getWebDriver(), getExpectedAlerts());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("§§URL§§path/to/img")
    public void baseURI_withBaseTag_relativePath() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html>\n"
                + "<head>\n"
                + "  <base href='img'>\n"
                + "</head>\n"
                + "<body>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "  log(document.baseURI);\n"
                + "</script>\n"
                + "</body></html>";

        expandExpectedAlertsVariables(URL_FIRST);
        loadPage2(html, new URL("http://localhost:" + PORT + "/path/to/page.html"));
        verifyTitle2(getWebDriver(), getExpectedAlerts());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("§§URL§§path/to/img/")
    public void baseURI_withBaseTag_relativePath_slash() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html>\n"
                + "<head>\n"
                + "  <base href='img/'>\n"
                + "</head>\n"
                + "<body>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "  log(document.baseURI);\n"
                + "</script>\n"
                + "</body></html>";

        expandExpectedAlertsVariables(URL_FIRST);
        loadPage2(html, new URL("http://localhost:" + PORT + "/path/to/page.html"));
        verifyTitle2(getWebDriver(), getExpectedAlerts());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("§§URL§§path/img")
    public void baseURI_withBaseTag_relativePath_parent() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html>\n"
                + "<head>\n"
                + "  <base href='../img'>\n"
                + "</head>\n"
                + "<body>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "  log(document.baseURI);\n"
                + "</script>\n"
                + "</body></html>";

        expandExpectedAlertsVariables(URL_FIRST);
        loadPage2(html, new URL("http://localhost:" + PORT + "/path/to/page.html"));
        verifyTitle2(getWebDriver(), getExpectedAlerts());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("§§URL§§img")
    public void baseURI_withBaseTag_relativePath_strange() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html>\n"
                + "<head>\n"
                + "  <base href='../../../../img'>\n"
                + "</head>\n"
                + "<body>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "  log(document.baseURI);\n"
                + "</script>\n"
                + "</body></html>";

        expandExpectedAlertsVariables(URL_FIRST);
        loadPage2(html, new URL("http://localhost:" + PORT + "/path/to/page.html"));
        verifyTitle2(getWebDriver(), getExpectedAlerts());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    @HtmlUnitNYI(CHROME = "false",
            EDGE = "false",
            FF = "false",
            FF_ESR = "false")
    public void hasFocus() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    log(document.hasFocus());\n"
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
    @Alerts(DEFAULT = "complete,[object HTMLBodyElement]-complete,[object HTMLBodyElement]-",
            FF = "uninitialized,[object HTMLBodyElement]-uninitialized,[object HTMLBodyElement]-",
            FF_ESR = "uninitialized,[object HTMLBodyElement]-uninitialized,[object HTMLBodyElement]-")
    @HtmlUnitNYI(CHROME = "loading,[object HTMLBodyElement]-complete,[object HTMLBodyElement]-",
            EDGE = "loading,[object HTMLBodyElement]-complete,[object HTMLBodyElement]-",
            FF = "loading,[object HTMLBodyElement]-complete,[object HTMLBodyElement]-",
            FF_ESR = "loading,[object HTMLBodyElement]-complete,[object HTMLBodyElement]-")
    public void readyState() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html>\n"
            + "<head>\n"
            + "  <script>\n"
            + "  var doc;\n"
            + "  function test() {\n"
            + "    var iframe = document.createElement('iframe');\n"
            + "    var textarea = document.getElementById('myTextarea');\n"
            + "    textarea.parentNode.appendChild(iframe);\n"
            + "    doc = iframe.contentWindow.document;\n"
            + "    check();\n"
            + "    setTimeout(check, 100);\n"
            + "  }\n"
            + "  function check() {\n"
            + "    var textarea = document.getElementById('myTextarea');\n"
            + "    textarea.value += doc.readyState + ',' + doc.body + '-';\n"
            + "  }\n"
            + "  </script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "<div>\n"
            + "  <textarea id='myTextarea' cols='80'></textarea>\n"
            + "</div>\n"
            + "</body>\n"
            + "</html>";

        final WebDriver driver = loadPage2(html);
        Thread.sleep(200);

        final List<String> actual = new LinkedList<>();
        actual.add(driver.findElement(By.id("myTextarea")).getDomProperty("value"));

        assertEquals(getExpectedAlerts(), actual);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("1")
    public void childElementCount() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    log(document.childElementCount);\n"
            + "  }\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "  <div/>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("TypeError")
    public void embeds() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    try {\n"
            + "      log(document.embeds(0));\n"
            + "    } catch(e) {logEx(e); }\n"
            + "  }\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "  <embed>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"1", "TypeError"})
    public void plugins() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    try {\n"
            + "      log(document.plugins.length);\n"
            + "      log(document.plugins(0));\n"
            + "    } catch(e) {logEx(e); }\n"
            + "  }\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "  <embed>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("TypeError")
    public void images() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    try {\n"
            + "      log(document.images(0));\n"
            + "    } catch(e) {logEx(e); }\n"
            + "  }\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "  <img>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("myBody")
    public void body() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "</script>\n"
            + "</head>\n"
            + "<body id='myBody' onload='log(document.body.id)'>\n"
            + "</body>\n"
            + "</html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("myFrameset")
    public void bodyFrameset() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "</script>\n"
            + "</head>\n"
            + "<frameset id='myFrameset' onload='log(document.body.id)'>\n"
            + "  <frame />\n"
            + "</frameset>\n"
            + "</html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"myBody", "newBody"})
    public void setBody() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    try {\n"
            + "      log(document.body.id);\n"

            + "      var newBody = document.createElement('body');\n"
            + "      newBody.id = 'newBody';\n"
            + "      document.body = newBody;\n"
            + "      log(document.body.id);\n"
            + "    } catch(e) {logEx(e); }\n"
            + "  }\n"
            + "</script>\n"
            + "</head>\n"
            + "<body id='myBody' onload='test()'>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"myBody", "HierarchyRequestError/DOMException"})
    public void setBodyDiv() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    try {\n"
            + "      log(document.body.id);\n"

            + "      var newDiv = document.createElement('div');\n"
            + "      newDiv.id = 'newDiv';\n"
            + "      document.body = newDiv;\n"
            + "      log(document.body.id);\n"
            + "    } catch(e) {logEx(e); }\n"
            + "  }\n"
            + "</script>\n"
            + "</head>\n"
            + "<body id='myBody' onload='test()'>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"myBody", "TypeError"})
    public void setBodyString() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    try {\n"
            + "      log(document.body.id);\n"

            + "      var newBody = '<body id=\"newBody\" onload=\"test()\"></body>';\n"
            + "      document.body = newBody;\n"
            + "      log(document.body.id);\n"
            + "    } catch(e) {logEx(e); }\n"
            + "  }\n"
            + "</script>\n"
            + "</head>\n"
            + "<body id='myBody' onload='test()'>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"myBody", "newFrameset"})
    public void setBodyFrameset() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    try {\n"
            + "      log(document.body.id);\n"

            + "      var newBody = document.createElement('frameset');\n"
            + "      newBody.id = 'newFrameset';\n"
            + "      document.body = newBody;\n"
            + "      log(document.body.id);\n"
            + "    } catch(e) {logEx(e); }\n"
            + "  }\n"
            + "</script>\n"
            + "</head>\n"
            + "<body id='myBody' onload='test()'>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * Property lastModified returns the last modification date of the document.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"string", "10/16/2009 09:59:47", "Fri, 16 Oct 2009 13:59:47 GMT"})
    @BuggyWebDriver(FF = {"string", "10/16/2009 15:59:47", "Fri, 16 Oct 2009 13:59:47 GMT"},
            FF_ESR = {"string", "10/16/2009 15:59:47", "Fri, 16 Oct 2009 13:59:47 GMT"})
    public void lastModified() throws Exception {
        final List<NameValuePair> responseHeaders = new ArrayList<>();
        responseHeaders.add(new NameValuePair("Last-Modified", "Fri, 16 Oct 2009 13:59:47 GMT"));
        testLastModified(responseHeaders, getBrowserVersion().getSystemTimezone().getID());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"string", "10/16/2009 13:59:47", "Fri, 16 Oct 2009 13:59:47 GMT"})
    @BuggyWebDriver(FF = {"string", "10/16/2009 15:59:47", "Fri, 16 Oct 2009 13:59:47 GMT"},
            FF_ESR = {"string", "10/16/2009 15:59:47", "Fri, 16 Oct 2009 13:59:47 GMT"})
    public void lastModifiedGMT() throws Exception {
        final List<NameValuePair> responseHeaders = new ArrayList<>();
        responseHeaders.add(new NameValuePair("Last-Modified", "Fri, 16 Oct 2009 13:59:47 GMT"));
        testLastModified(responseHeaders, "GMT");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"string", "10/16/2009 13:59:47", "Fri, 16 Oct 2009 13:59:47 GMT"})
    @BuggyWebDriver(FF = {"string", "10/16/2009 15:59:47", "Fri, 16 Oct 2009 13:59:47 GMT"},
            FF_ESR = {"string", "10/16/2009 15:59:47", "Fri, 16 Oct 2009 13:59:47 GMT"})
    public void lastModifiedUTC() throws Exception {
        final List<NameValuePair> responseHeaders = new ArrayList<>();
        responseHeaders.add(new NameValuePair("Last-Modified", "Fri, 16 Oct 2009 13:59:47 GMT"));
        testLastModified(responseHeaders, "UTC");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"string", "10/16/2009 15:59:47", "Fri, 16 Oct 2009 13:59:47 GMT"})
    @BuggyWebDriver(FF = {"string", "10/16/2009 15:59:47", "Fri, 16 Oct 2009 13:59:47 GMT"},
            FF_ESR = {"string", "10/16/2009 15:59:47", "Fri, 16 Oct 2009 13:59:47 GMT"})
    public void lastModifiedBerlin() throws Exception {
        final List<NameValuePair> responseHeaders = new ArrayList<>();
        responseHeaders.add(new NameValuePair("Last-Modified", "Fri, 16 Oct 2009 13:59:47 GMT"));
        testLastModified(responseHeaders, "Europe/Berlin");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"string", "10/16/2009 22:59:47", "Fri, 16 Oct 2009 13:59:47 GMT"})
    @BuggyWebDriver(FF = {"string", "10/16/2009 15:59:47", "Fri, 16 Oct 2009 13:59:47 GMT"},
            FF_ESR = {"string", "10/16/2009 15:59:47", "Fri, 16 Oct 2009 13:59:47 GMT"})
    public void lastModifiedJST() throws Exception {
        final List<NameValuePair> responseHeaders = new ArrayList<>();
        responseHeaders.add(new NameValuePair("Last-Modified", "Fri, 16 Oct 2009 13:59:47 GMT"));
        testLastModified(responseHeaders, "JST");
    }

    /**
     * Property lastModified returns the last modification date of the document.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"string", "10/16/2009 09:59:47", "Fri, 16 Oct 2009 13:59:47 GMT"})
    @BuggyWebDriver(FF = {"string", "10/16/2009 15:59:47", "Fri, 16 Oct 2009 13:59:47 GMT"},
            FF_ESR = {"string", "10/16/2009 15:59:47", "Fri, 16 Oct 2009 13:59:47 GMT"})
    public void lastModifiedAndDate() throws Exception {
        final List<NameValuePair> responseHeaders = new ArrayList<>();
        responseHeaders.add(new NameValuePair("Last-Modified", "Fri, 16 Oct 2009 13:59:47 GMT"));
        testLastModified(responseHeaders, getBrowserVersion().getSystemTimezone().getID());

        // Last-Modified header has priority compared to Date header
        responseHeaders.add(new NameValuePair("Date", "Fri, 17 Oct 2009 13:59:47 GMT"));
        testLastModified(responseHeaders, getBrowserVersion().getSystemTimezone().getID());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"string", "10/16/2009 13:59:47", "Fri, 16 Oct 2009 13:59:47 GMT"})
    @BuggyWebDriver(FF = {"string", "10/16/2009 15:59:47", "Fri, 16 Oct 2009 13:59:47 GMT"},
            FF_ESR = {"string", "10/16/2009 15:59:47", "Fri, 16 Oct 2009 13:59:47 GMT"})
    public void lastModifiedAndDateGMT() throws Exception {
        final List<NameValuePair> responseHeaders = new ArrayList<>();
        responseHeaders.add(new NameValuePair("Last-Modified", "Fri, 16 Oct 2009 13:59:47 GMT"));
        testLastModified(responseHeaders, "GMT");

        // Last-Modified header has priority compared to Date header
        responseHeaders.add(new NameValuePair("Date", "Fri, 17 Oct 2009 13:59:47 GMT"));
        testLastModified(responseHeaders, "GMT");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"string", "10/16/2009 13:59:47", "Fri, 16 Oct 2009 13:59:47 GMT"})
    @BuggyWebDriver(FF = {"string", "10/16/2009 15:59:47", "Fri, 16 Oct 2009 13:59:47 GMT"},
            FF_ESR = {"string", "10/16/2009 15:59:47", "Fri, 16 Oct 2009 13:59:47 GMT"})
    public void lastModifiedAndDateUTC() throws Exception {
        final List<NameValuePair> responseHeaders = new ArrayList<>();
        responseHeaders.add(new NameValuePair("Last-Modified", "Fri, 16 Oct 2009 13:59:47 GMT"));
        testLastModified(responseHeaders, "UTC");

        // Last-Modified header has priority compared to Date header
        responseHeaders.add(new NameValuePair("Date", "Fri, 17 Oct 2009 13:59:47 GMT"));
        testLastModified(responseHeaders, "UTC");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"string", "10/16/2009 15:59:47", "Fri, 16 Oct 2009 13:59:47 GMT"})
    public void lastModifiedAndDateBerlin() throws Exception {
        final List<NameValuePair> responseHeaders = new ArrayList<>();
        responseHeaders.add(new NameValuePair("Last-Modified", "Fri, 16 Oct 2009 13:59:47 GMT"));
        testLastModified(responseHeaders, "Europe/Berlin");

        // Last-Modified header has priority compared to Date header
        responseHeaders.add(new NameValuePair("Date", "Fri, 17 Oct 2009 13:59:47 GMT"));
        testLastModified(responseHeaders, "Europe/Berlin");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"string", "10/16/2009 22:59:47", "Fri, 16 Oct 2009 13:59:47 GMT"})
    @BuggyWebDriver(FF = {"string", "10/16/2009 15:59:47", "Fri, 16 Oct 2009 13:59:47 GMT"},
            FF_ESR = {"string", "10/16/2009 15:59:47", "Fri, 16 Oct 2009 13:59:47 GMT"})
    public void lastModifiedAndDateJST() throws Exception {
        final List<NameValuePair> responseHeaders = new ArrayList<>();
        responseHeaders.add(new NameValuePair("Last-Modified", "Fri, 16 Oct 2009 13:59:47 GMT"));
        testLastModified(responseHeaders, "JST");

        // Last-Modified header has priority compared to Date header
        responseHeaders.add(new NameValuePair("Date", "Fri, 17 Oct 2009 13:59:47 GMT"));
        testLastModified(responseHeaders, "JST");
    }

    /**
     * Property lastModified returns the last modification date of the document.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"string", "§§URL§§"})
    public void lastModifiedOnlyDate() throws Exception {
        final List<NameValuePair> responseHeaders = new ArrayList<>();
        responseHeaders.clear();
        responseHeaders.add(new NameValuePair("Date", "Fri, 16 Oct 2009 13:59:47 GMT"));

        expandExpectedAlertsVariables(DateUtils.formatDate(new Date()).substring(0, 17));
        final String html = DOCTYPE_HTML
                + "<html><head><script>\n"
                + LOG_TITLE_FUNCTION
                + "function doTest() {\n"
                + "  log(typeof document.lastModified);\n"
                + "  var d = new Date(document.lastModified);\n"
                + "  log(d.toGMTString().substr(0, 17));\n" // to have results not depending on the user's time zone
                + "}\n"
                + "</script></head>\n"
                + "<body onload='doTest()'>\n"
                + "</body></html>";

        getMockWebConnection().setResponse(URL_FIRST, html, 200, "OK", MimeType.TEXT_HTML, responseHeaders);

        final WebDriver driver = loadPage2(URL_FIRST, ISO_8859_1);
        verifyTitle2(driver, getExpectedAlerts());

        // for some strange reasons, the selenium driven browser is in an invalid
        // state after this test
        releaseResources();
        shutDownAll();
    }

    private void testLastModified(final List<NameValuePair> responseHeaders, final String tz) throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "function doTest() {\n"
            + "  log(typeof document.lastModified);\n"
            + "  log(document.lastModified);\n"
            + "  var d = new Date(document.lastModified);\n"
            + "  log(d.toGMTString());\n" // to have results not depending on the user's time zone
            + "}\n"
            + "</script></head>\n"
            + "<body onload='doTest()'>\n"
            + "</body></html>";

        shutDownAll();
        try {
            getMockWebConnection().setResponse(URL_FIRST, html, 200, "OK", MimeType.TEXT_HTML, responseHeaders);

            final BrowserVersion.BrowserVersionBuilder builder
                = new BrowserVersion.BrowserVersionBuilder(getBrowserVersion());
            builder.setSystemTimezone(TimeZone.getTimeZone(tz));
            setBrowserVersion(builder.build());

            final WebDriver driver = loadPage2(URL_FIRST, ISO_8859_1);
            verifyTitle2(driver, getExpectedAlerts());
        }
        finally {
            shutDownAll();
        }
    }

    /**
     * If neither Date header nor Last-Modified header is present, current time is taken.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"true", "true"})
    public void lastModified_noDateHeader() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "function doTest() {\n"
            + "  var justBeforeLoading = " + System.currentTimeMillis() + ";\n"
            + "  var d = new Date(document.lastModified);\n"
            + "  log(d.valueOf() >= justBeforeLoading - 1000);\n" // date string format has no ms, take 1s marge
            + "  log(d.valueOf() <= new Date().valueOf());\n"
            + "}\n"
            + "</script></head>\n"
            + "<body onload='doTest()'>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * Regression test for bug 2919853 (format of <tt>document.lastModified</tt> was incorrect).
     * @throws Exception if an error occurs
     */
    @Test
    public void lastModified_format() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><body onload='document.getElementById(\"i\").value = document.lastModified'>\n"
            + "<input id='i'></input></body></html>";

        final WebDriver driver = loadPageWithAlerts2(html);
        final String lastModified = driver.findElement(By.id("i")).getDomProperty("value");

        try {
            new SimpleDateFormat("MM/dd/yyyy HH:mm:ss", Locale.ROOT).parse(lastModified);
        }
        catch (final ParseException e) {
            fail(e.getMessage());
        }
    }
}
