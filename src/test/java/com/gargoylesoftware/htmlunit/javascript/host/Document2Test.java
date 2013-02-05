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
 */
@RunWith(BrowserRunner.class)
public class Document2Test extends WebDriverTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "exception", FF3_6 = "false", IE = "false")
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
    @Alerts(FF3_6 = { "DIV", "exception" },
            DEFAULT = "exception",
            IE = { "DIV", "false", "mySelect", "0", "OPTION", "myOption", "0" })
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
    @Alerts(FF = "false", IE = "true")
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
    @Alerts("1")
    @Browsers(FF)
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
    @Alerts({ "div1", "null", "2", "1" })
    @Browsers(FF)
    public void importNode_deep() throws Exception {
        importNode(true);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({ "div1", "null", "0" })
    @Browsers(FF)
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
                "undefined",
                "&lt;p&gt;a &amp; b&lt;/p&gt; &amp;amp; \u0162 \" '",
                "undefined" },
            FF17 = { "<p>a & b</p> &amp; \u0162 \" '",
                    "<p>a & b</p> &amp; \u0162 \" '",
                    "<div id=\"div\">&lt;p&gt;a &amp; b&lt;/p&gt; &amp;amp; \u0162 \" '</div>",
                    "&lt;p&gt;a &amp; b&lt;/p&gt; &amp;amp; \u0162 \" '",
                    "undefined" },
            IE = { "<p>a & b</p> &amp; \u0162 \" '",
                "<p>a & b</p> &amp; \u0162 \" '",
                "<DIV id=div>&lt;p&gt;a &amp; b&lt;/p&gt; &amp;amp; \u0162 \" '</DIV>",
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
    @Alerts(FF = { "error", "error", "true", "true", "true" },
            FF17 = { "error", "error", "false", "false", "false" },
            IE = { "true", "true", "true", "true", "true" })
    @NotYetImplemented(FF17)
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
}
