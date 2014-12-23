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
package com.gargoylesoftware.htmlunit.javascript.host.html;

import static com.gargoylesoftware.htmlunit.BrowserRunner.Browser.CHROME;
import static com.gargoylesoftware.htmlunit.BrowserRunner.Browser.FF;
import static com.gargoylesoftware.htmlunit.BrowserRunner.Browser.IE11;
import static com.gargoylesoftware.htmlunit.BrowserRunner.Browser.IE8;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;
import com.gargoylesoftware.htmlunit.BrowserRunner.NotYetImplemented;
import com.gargoylesoftware.htmlunit.WebDriverTestCase;
import com.gargoylesoftware.htmlunit.html.HtmlPageTest;

/**
 * Tests for {@link HTMLCollection}.
 *
 * @version $Revision$
 * @author Marc Guillemot
 * @author Ahmed Ashour
 * @author Frank Danek
 * @author Ronald Brill
 */
@RunWith(BrowserRunner.class)
public class HTMLCollectionTest extends WebDriverTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void testImplicitToStringConversion() throws Exception {
        final String html = "<html><head><title>foo</title><script>\n"
            + "function test() {\n"
            + "    alert(document.links != 'foo')\n"
            + "}\n"
            + "</script></head>\n"
            + "<body onload='test()'>\n"
            + "  <a href='bla.html'>link</a>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * Test that <tt>toString</tt> is accessible.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "function",
            IE8 = "object")
    @NotYetImplemented(IE8)
    public void testToStringFunction() throws Exception {
        final String html = "<html><head><title>foo</title><script>\n"
            + "function test() {\n"
            + "    alert(typeof document.links.toString);\n"
            + "}\n"
            + "</script></head><body onload='test()'>\n"
            + "<a href='bla.html'>link</a>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "5", "exception" },
            IE8 = { "5", "6" })
    public void getElements() throws Exception {
        final String html
            = "<html><head><title>foo</title><script>\n"
            + "function doTest() {\n"
            + "    alert(document.all.length);\n"
            + "    try {"
            + "      document.appendChild(document.createElement('div'));\n"
            + "      alert(document.all.length);\n"
            + "    } catch (e) { alert('exception') }\n"
            + "}\n"
            + "</script></head>\n"
            + "<body onload='doTest()'>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(FF24 = { "string 0", "string item", "string iterator", "string length",
                        "string myForm", "string namedItem" },
            FF31 = { "string 0", "string @@iterator", "string item", "string length", "string namedItem" },
            CHROME = { "string 0", "string item", "string length", "string myForm", "string namedItem" },
            IE = { "string length", "string myForm" },
            IE11 = { "string item", "string length", "string myForm", "string namedItem" })
    @NotYetImplemented({ FF, IE11, CHROME })
    public void testFor_in() throws Exception {
        final String html = "<html><head><title>foo</title><script>\n"
            + "  function test() {\n"
            + "    var arr = new Array();\n"
            + "    \n"
            + "    for (i in document.forms) {\n"
            + "      arr[arr.length] = (typeof i) + ' ' + i;\n"
            + "    }\n"
            + "    arr.sort();\n"
            + "    for (i = 0; i < arr.length; i++) {\n"
            + "       alert(arr[i]);\n"
            + "    }\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "<form name='myForm'></form>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(FF24 = { "string 0", "string 1", "string 2", "string 3", "string 4", "string 5",
                "string action", "string first_submit", "string id1", "string input_disabled",
                "string item", "string iterator", "string length", "string namedItem", "string second_submit",
                "string val1", "string val2" },
            FF31 = { "string 0", "string 1", "string 2", "string 3", "string 4", "string 5",
                    "string @@iterator", "string item", "string length", "string namedItem" },
            CHROME = { "string 0", "string 1", "string 2", "string 3", "string 4", "string 5",
                 "string action", "string first_submit", "string id1", "string input_disabled",
                 "string item", "string length", "string namedItem", "string second_submit",
                 "string val1, string val2" },
            IE8 = { "string 1", "string action", "string first_submit", "string length",
                "string second_submit", "string val1", "string val2" },
            IE11 = { "string 1", "string action", "string first_submit", "string item",
                "string length", "string namedItem", "string second_submit", "string val1",
                "string val2" })
    @NotYetImplemented({ FF, IE11, CHROME })
    public void testFor_in2() throws Exception {
        final String html = "<html><head><title>foo</title><script>\n"
            + "  function test() {\n"
            + "    var form = document.getElementById('myForm');\n"
            + "    var x = form.getElementsByTagName('*');\n"
            + "    var arr = new Array();\n"
            + "    for (i in x){\n"
            + "      arr[arr.length] = (typeof i) + ' ' + i;\n"
            + "    }\n"
            + "    arr.sort();\n"
            + "    for (i = 0; i < arr.length; i++) {\n"
            + "      alert(arr[i]);\n"
            + "    }\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "<form id='myForm'>\n"
            + "  <input type='text' id='id1' name='val1' id='input_enabled' value='4'>\n"
            + "  <div>This is not a form element</div>\n"
            + "  <input type='text' name='val2' id='input_disabled' disabled='disabled' value='5'>\n"
            + "  <input type='submit' name='first_submit' value='Commit it!'>\n"
            + "  <input type='submit' id='second_submit' value='Delete it!'>\n"
            + "  <input type='text' name='action' value='blah'>\n"
            + "</form>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * <code>document.all.tags</code> is different from <code>document.forms.tags</code>!
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"false", "false" },
            IE = {"true", "true" })
    public void testTags() throws Exception {
        final String html = "<html><head><title>foo</title><script>\n"
            + "  function test() {\n"
            + "    alert(document.all.tags != undefined);\n"
            + "    alert(document.forms.tags != undefined);\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "<form name='myForm'></form>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * Depending on the method used, out of bound access give different responses.
     * @throws Exception if the test fails
     */
    @Test
    @NotYetImplemented({ FF, CHROME })
    @Alerts(DEFAULT = { "null", "null", "undefined", "exception" },
            IE = { "null", "null", "undefined", "null" },
            IE11 = { "null", "null", "undefined", "undefined" })
    public void testOutOfBoundAccess() throws Exception {
        final String html = "<html><head><title>foo</title><script>\n"
            + "  function test() {\n"
            + "    var col = document.getElementsByTagName('a');\n"
            + "    alert(col.item(1));\n"
            + "    alert(col.namedItem('foo'));\n"
            + "    alert(col[1]);\n"
            + "    try {\n"
            + "      alert(col(1));\n"
            + "    } catch (e) { alert('exception') }\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({ "undefined", "undefined", "undefined" })
    public void testInexistentProperties() throws Exception {
        final String html = "<html><head><script>\n"
            + "  function test() {\n"
            + "    var x = document.documentElement.childNodes;\n"
            + "    alert(x.split);\n"
            + "    alert(x.setInterval);\n"
            + "    alert(x.bogusNonExistentProperty);\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";
        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "3", "#text", "5" },
            IE8 = { "1", "DIV", "2" })
    public void childNodes() throws Exception {
        final String html = "<html><head>\n"
            + "<script>\n"
            + "  function test() {\n"
            + "    alert(document.body.childNodes.length);\n"
            + "    alert(document.body.firstChild.nodeName);\n"
            + "    alert(document.getElementById('myDiv').childNodes.length);\n"
            + "  }\n"
            + "</script></head>\n"
            + "<body onload='test()'> <div id='myDiv'> <div> </div> <div> </div> </div> </body>\n"
            + "</html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("object")
    public void typeof() throws Exception {
        final String html = "<html><head>\n"
            + "<script>\n"
            + "  function test() {\n"
            + "    alert(typeof document.getElementsByTagName('a'));\n"
            + "  }\n"
            + "</script></head>\n"
            + "<body onload='test()'></body>\n"
            + "</html>";

        loadPageWithAlerts2(html);
    }

    /**
     * Verifies that dollar signs don't cause exceptions in {@link HTMLCollection} (which uses Java
     * regex internally). Found via the MooTools unit tests.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "[object HTMLHeadingElement]", "undefined" },
            IE8 = { "[object]", "undefined" })
    public void getElementWithDollarSign() throws Exception {
        final String html
            = "<h3 id='$h'>h</h3><script>\n"
            + "var hs = document.getElementsByTagName('h3');\n"
            + "alert(hs['$h']);\n"
            + "alert(hs['$n']);\n"
            + "</script>";
        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT =  { "function", "function", "function", "function" },
            IE8 = { "undefined", "undefined", "undefined", "undefined" })
    public void array_prototype() throws Exception {
        final String html = "<html><head>\n"
            + "<script>\n"
            + "  function test() {\n"
            + "    alert(typeof Object.prototype.__defineGetter__);\n"
            + "    alert(typeof Object.prototype.__lookupGetter__);\n"
            + "    alert(typeof Array.prototype.indexOf);\n"
            + "    alert(typeof Array.prototype.map);\n"
            + "  }\n"
            + "</script></head>\n"
            + "<body onload='test()'></body>\n"
            + "</html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT =  { "function", "function", "function", "function" },
            IE8 = { "undefined", "undefined", "function", "function" })
    public void array_prototype_standards() throws Exception {
        final String html = HtmlPageTest.STANDARDS_MODE_PREFIX_ + "<html><head>\n"
            + "<script>\n"
            + "  function test() {\n"
            + "    alert(typeof Object.prototype.__defineGetter__);\n"
            + "    alert(typeof Object.prototype.__lookupGetter__);\n"
            + "    alert(typeof Array.prototype.indexOf);\n"
            + "    alert(typeof Array.prototype.map);\n"
            + "  }\n"
            + "</script></head>\n"
            + "<body onload='test()'></body>\n"
            + "</html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void has() throws Exception {
        final String html = "<html><head><title>foo</title><script>\n"
            + "  function test() {\n"
            + "    alert(0 in document.forms);\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "<form name='myForm'></form>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "b1-button1",
            IE11 = "null")
    public void item_Unknown() throws Exception {
        item("'foo'");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "b1-button1",
            IE11 = "b2-button2")
    public void item_ById() throws Exception {
        item("'b2'");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "b1-button1",
            IE11 = "b2-button2")
    public void item_ByName() throws Exception {
        item("'button2'");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "null",
            IE8 = "exception")
    public void item_NegativIndex() throws Exception {
        item("-1");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("b1-button1")
    public void item_ZeroIndex() throws Exception {
        item("0");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("b2-button2")
    public void item_ValidIndex() throws Exception {
        item("1");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("b2-button2")
    public void item_DoubleIndex() throws Exception {
        item("1.1");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("null")
    public void item_InvalidIndex() throws Exception {
        item("2");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "b2-button2",
            IE11 = "null")
    public void item_IndexAsString() throws Exception {
        item("'1'");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "b2-button2",
            IE11 = "null")
    public void item_IndexDoubleAsString() throws Exception {
        item("'1.1'");
    }

    private void item(final String name) throws Exception {
        final String html
            = "<!doctype html>\n"
            + "<html><head><title>First</title><script>\n"
            + "  function report(result) {\n"
            + "    if (result == null || result == undefined) {\n"
            + "      alert(result);\n"
            + "    } else if (('length' in result) && ('item' in result)) {\n"
            + "      alert('coll ' + result.length);\n"
            + "      for(i=0; i < result.length; i++) {\n"
            + "        alert(result.item(i).id + '-' + result.item(i).name);\n"
            + "      }\n"
            + "    } else if (result.id || result.name) {\n"
            + "      alert(result.id + '-' + result.name);\n"
            + "    } else {\n"
            + "      alert(result);\n"
            + "    }\n"
            + "  }\n"

            + "  function doTest() {\n"
            + "    try {\n"
            + "      var col = document.getElementsByTagName('button');\n"
            + "      report(col.item(" + name + "));\n"
            + "    } catch(e) { alert('exception'); }"
            + "  }\n"
            + "</script></head>\n"
            + "<body onload='doTest()'>\n"
            + "  <button id='b1' name='button1'></button>\n"
            + "  <button id='b2' name='button2'></button>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("undefined")
    public void arrayIndex_Unknown() throws Exception {
        arrayIndex("'foo'");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("b2-button2")
    public void arrayIndex_ById() throws Exception {
        arrayIndex("'b2'");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("b2-button2")
    public void arrayIndex_ByName() throws Exception {
        arrayIndex("'button2'");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("undefined")
    public void arrayIndex_NegativIndex() throws Exception {
        arrayIndex("-1");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("b1-button1")
    public void arrayIndex_ZeroIndex() throws Exception {
        arrayIndex("0");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("b2-button2")
    public void arrayIndex_ValidIndex() throws Exception {
        arrayIndex("1");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "undefined",
            IE = "b2-button2")
    public void arrayIndex_DoubleIndex() throws Exception {
        arrayIndex("1.1");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("undefined")
    public void arrayIndex_InvalidIndex() throws Exception {
        arrayIndex("2");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("undefined")
    public void arrayIndex_IndexAsString() throws Exception {
        arrayIndex("'2'");
    }

    private void arrayIndex(final String name) throws Exception {
        final String html
            = "<!doctype html>\n"
            + "<html><head><title>First</title><script>\n"
            + "  function report(result) {\n"
            + "    if (result == null || result == undefined) {\n"
            + "      alert(result);\n"
            + "    } else if (('length' in result) && ('item' in result)) {\n"
            + "      alert('coll ' + result.length);\n"
            + "      for(i=0; i < result.length; i++) {\n"
            + "        alert(result.item(i).id + '-' + result.item(i).name);\n"
            + "      }\n"
            + "    } else if (result.id || result.name) {\n"
            + "      alert(result.id + '-' + result.name);\n"
            + "    } else {\n"
            + "      alert(result);\n"
            + "    }\n"
            + "  }\n"

            + "  function doTest() {\n"
            + "    try {\n"
            + "      var col = document.getElementsByTagName('button');\n"
            + "      report(col[" + name + "]);\n"
            + "    } catch(e) { alert('exception'); }"
            + "  }\n"
            + "</script></head>\n"
            + "<body onload='doTest()'>\n"
            + "  <button id='b1' name='button1'></button>\n"
            + "  <button id='b2' name='button2'></button>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("null")
    public void namedItem_Unknown() throws Exception {
        namedItem("foo");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("button1-")
    public void namedItem_ById() throws Exception {
        namedItem("button1");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("-button2")
    public void namedItem_ByName_formWithoutId() throws Exception {
        namedItem("button2");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("b3-button3")
    public void namedItem_ByName() throws Exception {
        namedItem("button3");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("b4-button4_1")
    public void namedItem_DuplicateId() throws Exception {
        namedItem("b4");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("b5_1-button5")
    public void namedItem_DuplicateName() throws Exception {
        namedItem("button5");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "b6-button6",
            CHROME = "button6-button6_2")
    public void namedItem_DuplicateIdName() throws Exception {
        namedItem("button6");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("b1-button1")
    public void namedItem_ZeroIndex() throws Exception {
        item("0");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("b2-button2")
    public void namedItem_ValidIndex() throws Exception {
        item("1");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("b2-button2")
    public void namedItem_DoubleIndex() throws Exception {
        item("1.1");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("null")
    public void namedItem_InvalidIndex() throws Exception {
        item("200");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "b2-button2",
            IE11 = "null")
    public void namedItem_IndexAsString() throws Exception {
        item("'1'");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "b2-button2",
            IE11 = "null")
    public void namedItem_IndexDoubleAsString() throws Exception {
        item("'1.1'");
    }

    private void namedItem(final String name) throws Exception {
        final String html
            = "<!doctype html>\n"
            + "<html><head><title>First</title><script>\n"
            + "  function report(result) {\n"
            + "    if (result == null || result == undefined) {\n"
            + "      alert(result);\n"
            + "    } else if (result.id || result.name) {\n"
            + "      alert(result.id + '-' + result.name);\n"
            + "    } else {\n"
            + "      alert('coll ' + result.length);\n"
            + "      for(i=0; i < result.length; i++) {\n"
            + "        alert(result.item(i).id + '-' + result.item(i).name);\n"
            + "      }\n"
            + "    }\n"
            + "   }\n"

            + "  function doTest() {\n"
            + "    var col = document.getElementsByTagName('button');\n"
            + "    report(col.namedItem('" + name + "'));\n"
            + "  }\n"
            + "</script></head>\n"
            + "<body onload='doTest()'>\n"
            + "  <button id='button1'></button>\n"
            + "  <button name='button2'></button>\n"
            + "  <button id='b3' name='button3'></button>\n"
            + "  <button id='b4' name='button4_1'></button>\n"
            + "  <button id='b4' name='button4_2'></button>\n"
            + "  <button id='b5_1' name='button5'></button>\n"
            + "  <button id='b5_2' name='button5'></button>\n"
            + "  <button id='b6' name='button6'></button>\n"
            + "  <button id='button6' name='button6_2'></button>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }
}
