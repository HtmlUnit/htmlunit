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
import static com.gargoylesoftware.htmlunit.BrowserRunner.Browser.FF17;
import static com.gargoylesoftware.htmlunit.BrowserRunner.Browser.FF24;
import static com.gargoylesoftware.htmlunit.BrowserRunner.Browser.IE10;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;
import com.gargoylesoftware.htmlunit.BrowserRunner.Browsers;
import com.gargoylesoftware.htmlunit.BrowserRunner.NotYetImplemented;
import com.gargoylesoftware.htmlunit.MockWebConnection;
import com.gargoylesoftware.htmlunit.WebDriverTestCase;

/**
 * Tests for {@link Document}.
 *
 * @version $Revision$
 * @author Ronald Brill
 * @author Marc Guillemot
 * @author Frank Danek
 */
@RunWith(BrowserRunner.class)
public class Document2Test extends WebDriverTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "exception",
            IE8 = "false")
    public void createElementWithAngleBrackets() throws Exception {
        final String html = "<html><head><title>foo</title><script>\n"
            + "  function test() {\n"
            + "    try {\n"
            + "      var select = document.createElement('<select>');\n"
            + "      alert(select.add == undefined);\n"
            + "    }\n"
            + "    catch (e) { alert('exception') }\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";
        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "exception",
            IE8 = { "DIV", "false", "mySelect", "0", "OPTION", "myOption", "0" })
    public void createElementWithHtml() throws Exception {
        final String html = "<html><head><title>foo</title><script>\n"
            + "  function test() {\n"
            + "    try {\n"
            + "      alert(document.createElement('<div>').tagName);\n"
            + "      var select = document.createElement(\"<select id='mySelect'><option>hello</option>\");\n"
            + "      alert(select.add == undefined);\n"
            + "      alert(select.id);\n"
            + "      alert(select.childNodes.length);\n"
            + "      var option = document.createElement(\"<option id='myOption'>\");\n"
            + "      alert(option.tagName);\n"
            + "      alert(option.id);\n"
            + "      alert(option.childNodes.length);\n"
            + "    }\n"
            + "    catch (e) { alert('exception') }\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * Dedicated test for 3410657.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            IE8 = "true")
    public void createElementPrototype() throws Exception {
        final String html = "<html><head><title>foo</title><script>\n"
            + "  var HAS_EXTENDED_CREATE_ELEMENT_SYNTAX = (function() {\n"
            + "    try {\n"
            + "      var el = document.createElement('<input name=\"x\">');\n"
            + "      return el.tagName.toLowerCase() === 'input' && el.name === 'x';\n"
            + "    } catch (err) {\n"
            + "      return false;\n"
            + "    }\n"
            + "  })();\n"
            + "  alert(HAS_EXTENDED_CREATE_ELEMENT_SYNTAX)\n"
            + "</script></head><body>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Browsers({ FF, IE10 })
    @Alerts("1")
    public void getElementByTagNameNS_includesHtml() throws Exception {
        final String html
            = "<html><head><title>foo</title>"
            + "<script>\n"
            + "  function doTest(){\n"
            + "    alert(document.getElementsByTagNameNS('*', 'html').length);\n"
            + "  }\n"
            + "</script>"
            + "</head>\n"
            + "<body onload='doTest()'>\n"
            + "  <p>hello world</p>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Browsers({ FF, IE10 })
    @Alerts({ "div1", "null", "2", "1" })
    public void importNode_deep() throws Exception {
        importNode(true);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Browsers({ FF, IE10 })
    @Alerts({ "div1", "null", "0" })
    public void importNode_notDeep() throws Exception {
        importNode(false);
    }

    private void importNode(final boolean deep) throws Exception {
        final String html = "<html><head><title>foo</title><script>\n"
            + "  function test() {\n"
            + "    var node = document.importNode(document.getElementById('div1'), " + deep + ");\n"
            + "    alert(node.id);\n"
            + "    alert(node.parentNode);\n"
            + "    alert(node.childNodes.length);\n"
            + "    if (node.childNodes.length != 0)\n"
            + "      alert(node.childNodes[0].childNodes.length);\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "  <div id='div1'><div id='div1_1'><div id='div1_1_1'></div></div><div id='div1_2'></div></div>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * Test for issue 3560821.
     * @throws Exception if the test fails
     */
    @Test
    @Browsers(FF)
    @Alerts({"parent", "child" })
    public void importNodeWithNamespace() throws Exception {
        final MockWebConnection conn = getMockWebConnection();
        conn.setDefaultResponse(
                "<?xml version=\"1.0\"?><html xmlns=\"http://www.w3.org/1999/xhtml\"><div id='child'> </div></html>",
                200, "OK", "text/xml");

        final String html = "<html xmlns='http://www.w3.org/1999/xhtml'>"
            + "<head><title>foo</title><script>\n"
            + "function test() {\n"
            + "   var xmlhttp = new XMLHttpRequest();\n"
            + "   xmlhttp.open(\"GET\",\"content.xhtml\",true);\n"
            + "   xmlhttp.send();\n"
            + "   xmlhttp.onreadystatechange = function() {\n"
            + "     if (xmlhttp.readyState==4 && xmlhttp.status==200) {\n"
            + "       var child = document.importNode(xmlhttp.responseXML.getElementById(\"child\"), true);\n"
            + "       document.getElementById(\"parent\").appendChild(child);\n"
            + "       var found = document.evaluate(\"//div[@id='parent']\", document, null,"
            +                       "XPathResult.FIRST_ORDERED_NODE_TYPE, null);\n"
            + "       alert(found.singleNodeValue.id);\n"
            + "       found = document.evaluate(\"//div[@id='child']\", document, null,"
            +                       "XPathResult.FIRST_ORDERED_NODE_TYPE, null);\n"
            + "       alert(found.singleNodeValue.id);\n"
            + "     }\n"
            + "  }\n"
            + "}\n"
            + "</script></head>\n"
            + "<body onload='test()'>\n"
            + "  <div id='parent'></div>\n"
            + "</body></html>\n";

        loadPageWithAlerts2(html, 200);
    }

    /**
     * Test for issue 3560821.
     * @throws Exception if the test fails
     */
    @Test
    @Browsers(FF)
    @Alerts({"parent", "child", "child3" })
    public void importNodesWithNamespace() throws Exception {
        final MockWebConnection conn = getMockWebConnection();
        conn.setDefaultResponse(
                "<?xml version=\"1.0\"?><html xmlns=\"http://www.w3.org/1999/xhtml\">"
                + "<div id='child'><div id='child2'><div id='child3'>-</div></div></div></html>",
                200, "OK", "text/xml");

        final String html = "<html xmlns='http://www.w3.org/1999/xhtml'>"
            + "<head><title>foo</title><script>\n"
            + "function test() {\n"
            + "   var xmlhttp = new XMLHttpRequest();\n"
            + "   xmlhttp.open(\"GET\",\"content.xhtml\",true);\n"
            + "   xmlhttp.send();\n"
            + "   xmlhttp.onreadystatechange = function() {\n"
            + "     if (xmlhttp.readyState==4 && xmlhttp.status==200) {\n"
            + "       var child = document.importNode(xmlhttp.responseXML.getElementById(\"child\"), true);\n"
            + "       document.getElementById(\"parent\").appendChild(child);\n"
            + "       var found = document.evaluate(\"//div[@id='parent']\", document, null,"
            +                       "XPathResult.FIRST_ORDERED_NODE_TYPE, null);\n"
            + "       alert(found.singleNodeValue.id);\n"
            + "       found = document.evaluate(\"//div[@id='child']\", document, null,"
            +                       "XPathResult.FIRST_ORDERED_NODE_TYPE, null);\n"
            + "       alert(found.singleNodeValue.id);\n"
            + "       found = document.evaluate(\"//div[@id='child3']\", document, null,"
            +                       "XPathResult.FIRST_ORDERED_NODE_TYPE, null);\n"
            + "       alert(found.singleNodeValue.id);\n"
            + "     }\n"
            + "  }\n"
            + "}\n"
            + "</script></head>\n"
            + "<body onload='test()'>\n"
            + "  <div id='parent'></div>\n"
            + "</body></html>\n";

        loadPageWithAlerts2(html, 200);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({ "null", "text1" })
    public void activeElement() throws Exception {
        final String html = "<html><head><script>\n"
            + "  alert(document.activeElement);"
            + "  function test() {\n"
            + "     alert(document.activeElement.id);\n"
            + "  }\n"
            + "</script></head>\n"
            + "<body>\n"
            + "<input id='text1' onclick='test()'>\n"
            + "</body></html>";

        final WebDriver driver = loadPage2(html);
        driver.findElement(By.id("text1")).click();

        assertEquals(getExpectedAlerts(), getCollectedAlerts(driver));
    }

    /**
     * Verifies that when we create a text node and append it to an existing DOM node,
     * its <tt>outerHTML</tt>, <tt>innerHTML</tt> and <tt>innerText</tt> properties are
     * properly escaped.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(FF = { "<p>a & b</p> &amp; \u0162 \" '",
                    "<p>a & b</p> &amp; \u0162 \" '",
                    "<div id=\"div\">&lt;p&gt;a &amp; b&lt;/p&gt; &amp;amp; \u0162 \" '</div>",
                    "&lt;p&gt;a &amp; b&lt;/p&gt; &amp;amp; \u0162 \" '",
                    "undefined" },
            IE = { "<p>a & b</p> &amp; \u0162 \" '",
                "<p>a & b</p> &amp; \u0162 \" '",
                "<DIV id=div>&lt;p&gt;a &amp; b&lt;/p&gt; &amp;amp; \u0162 \" '</DIV>",
                "&lt;p&gt;a &amp; b&lt;/p&gt; &amp;amp; \u0162 \" '",
                "<p>a & b</p> &amp; \u0162 \" '" },
            IE10 = { "<p>a & b</p> &amp; \u0162 \" '",
                "<p>a & b</p> &amp; \u0162 \" '",
                "<div id=\"div\">&lt;p&gt;a &amp; b&lt;/p&gt; &amp;amp; \u0162 \" '</div>",
                "&lt;p&gt;a &amp; b&lt;/p&gt; &amp;amp; \u0162 \" '",
                "<p>a & b</p> &amp; \u0162 \" '" })
    public void createTextNodeWithHtml_FF() throws Exception {
        final String html = "<html><body onload='test()'><script>\n"
            + "   function test() {\n"
            + "      var node = document.createTextNode('<p>a & b</p> &amp; \\u0162 \" \\'');\n"
            + "      alert(node.data);\n"
            + "      alert(node.nodeValue);\n"
            + "      var div = document.getElementById('div');\n"
            + "      div.appendChild(node);\n"
            + "      alert(div.outerHTML);\n"
            + "      alert(div.innerHTML);\n"
            + "      alert(div.innerText);\n"
            + "   };\n"
            + "</script>\n"
            + "<div id='div'></div>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF = { "error", "error", "false", "false", "false" },
            IE = { "true", "true", "true", "true", "true" })
    @NotYetImplemented({ FF17, FF24 })
    public void queryCommandEnabled() throws Exception {
        final String html = "<html><body onload='x()'><iframe name='f' id='f'></iframe><script>\n"
            + "function x() {\n"
            + "var d = window.frames['f'].document;\n"
            + "try { alert(d.queryCommandEnabled('SelectAll')); } catch(e) { alert('error'); }\n"
            + "try { alert(d.queryCommandEnabled('sElectaLL')); } catch(e) { alert('error'); }\n"
            + "d.designMode='on';\n"
            + "alert(d.queryCommandEnabled('SelectAll'));\n"
            + "alert(d.queryCommandEnabled('selectall'));\n"
            + "alert(d.queryCommandEnabled('SeLeCtALL'));}\n"
            + "</script></body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({ "bar", "null", "null" })
    public void getElementById() throws Exception {
        final String html
            = "<html><head><title>First</title><script>\n"
            + "function doTest() {\n"
            + "    alert(top.document.getElementById('input1').value);\n"
            + "    alert(document.getElementById(''));\n"
            + "    alert(document.getElementById('non existing'));\n"
            + "}\n"
            + "</script></head><body onload='doTest()'>\n"
            + "<form id='form1'>\n"
            + "<input id='input1' name='foo' type='text' value='bar' />\n"
            + "</form>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({ "bar", "null" })
    public void getElementById_resetId() throws Exception {
        final String html
            = "<html><head><title>First</title><script>\n"
            + "function doTest() {\n"
            + "    var input1=top.document.getElementById('input1');\n"
            + "    input1.id='newId';\n"
            + "    alert(top.document.getElementById('newId').value);\n"
            + "    alert(top.document.getElementById('input1'));\n"
            + "}\n"
            + "</script></head><body onload='doTest()'>\n"
            + "<form id='form1'>\n"
            + "<input id='input1' name='foo' type='text' value='bar' />\n"
            + "</form>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("bar")
    public void getElementById_setNewId() throws Exception {
        final String html
            = "<html><head><title>First</title><script>\n"
            + "function doTest() {\n"
            + "    var div1 = document.getElementById('div1');\n"
            + "    div1.firstChild.id='newId';\n"
            + "    alert(document.getElementById('newId').value);\n"
            + "}\n"
            + "</script></head><body onload='doTest()'>\n"
            + "<form id='form1'>\n"
            + "<div id='div1'><input name='foo' type='text' value='bar'></div>\n"
            + "</form>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * Regression test for bug 740665.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("id1")
    public void getElementById_divId() throws Exception {
        final String html
            = "<html><head><title>First</title><script>\n"
            + "function doTest() {\n"
            + "    var element = document.getElementById('id1');\n"
            + "    alert(element.id);\n"
            + "}\n"
            + "</script></head><body onload='doTest()'>\n"
            + "<div id='id1'></div></body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * Regression test for bug 740665.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("script1")
    public void getElementById_scriptId() throws Exception {
        final String html
            = "<html><head><title>First</title><script id='script1'>\n"
            + "function doTest() {\n"
            + "    alert(top.document.getElementById('script1').id);\n"
            + "}\n"
            + "</script></head><body onload='doTest()'>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({ "first", "newest" })
    public void getElementById_alwaysFirstOneInDocumentOrder() throws Exception {
        final String html = "<html><body>\n"
            + "<span id='it' class='first'></span>\n"
            + "<span id='it' class='second'></span>\n"
            + "<script>\n"
            + "alert(document.getElementById('it').className);\n"
            + "var s = document.createElement('span');\n"
            + "s.id = 'it';\n"
            + "s.className = 'newest';\n"
            + "document.body.insertBefore(s, document.body.firstChild);\n"
            + "alert(document.getElementById('it').className);\n"
            + "</script></body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { },
            IE = { "style.css", "LINK" },
            IE10 = { "§§URL§§style.css", "LINK" })
    public void createStyleSheet() throws Exception {
        final String html
            = "<html><head><title>First</title>\n"
            + "<script>\n"
            + "  function doTest() {\n"
            + "    if (document.createStyleSheet) {\n"
            + "      document.createStyleSheet('style.css');\n"
            + "      for (var si = 0; si < document.styleSheets.length; si++) {\n"
            + "        var sheet = document.styleSheets[si];\n"
            + "        alert(sheet.href);\n"
            + "        alert(sheet.owningElement.tagName);\n"
            + "      }\n"
            + "    }\n"
            + "  }\n"
            + "</script></head>\n"
            + "<body onload='doTest()'>\n"
            + "  <div id='id1'></div>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { },
            IE = { "null", "" },
            IE10 = { "§§URL§§null", "null" })
    public void createStyleSheet_emptyUrl() throws Exception {
        final String html
            = "<html><head><title>First</title>\n"
            + "<script>\n"
            + "  function doTest() {\n"
            + "    if (document.createStyleSheet) {\n"
            + "      document.createStyleSheet(null);\n"
            + "      document.createStyleSheet('');\n"
            + "      for (var si = 0; si < document.styleSheets.length; si++) {\n"
            + "        var sheet = document.styleSheets[si];\n"
            + "        alert(sheet.href);\n"
            + "      }\n"
            + "    }\n"
            + "  }\n"
            + "</script></head>\n"
            + "<body onload='doTest()'>\n"
            + "  <div id='id1'></div>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { },
            IE = { "dotseven.css", "zero.css", "minus1.css", "seven.css", "none.css" },
            IE10 = { "§§URL§§dotseven.css", "§§URL§§zero.css", "§§URL§§minus1.css", "§§URL§§seven.css",
                        "§§URL§§none.css" })
    public void createStyleSheet_insertAt() throws Exception {
        final String html
            = "<html><head><title>First</title>\n"
            + "<script>\n"
            + "  function doTest() {\n"
            + "    if (document.createStyleSheet) {\n"
            + "      document.createStyleSheet('minus1.css', -1);\n"
            + "      document.createStyleSheet('zero.css', 0);\n"
            + "      document.createStyleSheet('dotseven.css', 0.7);\n"
            + "      document.createStyleSheet('seven.css', 7);\n"
            + "      document.createStyleSheet('none.css');\n"
            + "      for (var si = 0; si < document.styleSheets.length; si++) {\n"
            + "        var sheet = document.styleSheets[si];\n"
            + "        alert(sheet.href);\n"
            + "      }\n"
            + "    }\n"
            + "  }\n"
            + "</script></head>\n"
            + "<body onload='doTest()'>\n"
            + "  <div id='id1'></div>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }
}
