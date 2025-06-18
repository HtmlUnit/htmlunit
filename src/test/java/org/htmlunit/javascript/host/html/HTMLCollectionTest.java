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

import org.htmlunit.WebDriverTestCase;
import org.htmlunit.junit.annotation.Alerts;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link HTMLCollection}.
 *
 * @author Marc Guillemot
 * @author Ahmed Ashour
 * @author Frank Danek
 * @author Ronald Brill
 */
public class HTMLCollectionTest extends WebDriverTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void implicitToStringConversion() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "function test() {\n"
            + "  log(document.links != 'foo')\n"
            + "}\n"
            + "</script></head>\n"
            + "<body onload='test()'>\n"
            + "  <a href='bla.html'>link</a>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * Test that <tt>toString</tt> is accessible.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("function")
    public void toStringFunction() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "function test() {\n"
            + "  log(typeof document.links.toString);\n"
            + "}\n"
            + "</script></head>\n"
            + "<body onload='test()'>\n"
            + "<a href='bla.html'>link</a>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"4", "HierarchyRequestError/DOMException"})
    public void getElements() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "function doTest() {\n"
            + "  log(document.all.length);\n"
            + "  try {\n"
            + "    document.appendChild(document.createElement('div'));\n"
            + "    log(document.all.length);\n"
            + "  } catch(e) { logEx(e) }\n"
            + "}\n"
            + "</script></head>\n"
            + "<body onload='doTest()'>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"string 0", "string item", "string length", "string namedItem"})
    public void for_in() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var arr = new Array();\n"
            + "    \n"
            + "    for (i in document.forms) {\n"
            + "      arr[arr.length] = (typeof i) + ' ' + i;\n"
            + "    }\n"
            + "    arr.sort();\n"
            + "    for (i = 0; i < arr.length; i++) {\n"
            + "      log(arr[i]);\n"
            + "    }\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "<form name='myForm'></form>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"string 0", "string 1", "string 2", "string 3", "string 4", "string 5",
             "string item", "string length", "string namedItem"})
    public void for_in2() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var form = document.getElementById('myForm');\n"
            + "    var x = form.getElementsByTagName('*');\n"
            + "    var arr = new Array();\n"
            + "    for (i in x){\n"
            + "      arr[arr.length] = (typeof i) + ' ' + i;\n"
            + "    }\n"
            + "    arr.sort();\n"
            + "    for (i = 0; i < arr.length; i++) {\n"
            + "      log(arr[i]);\n"
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

        loadPageVerifyTitle2(html);
    }

    /**
     * <code>document.all.tags</code> is different from <code>document.forms.tags</code>!
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"false", "false"})
    public void tags() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    log(document.all.tags != undefined);\n"
            + "    log(document.forms.tags != undefined);\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "<form name='myForm'></form>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * Depending on the method used, out of bound access give different responses.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"null", "null", "undefined", "TypeError"})
    public void outOfBoundAccess() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var col = document.getElementsByTagName('a');\n"
            + "    log(col.item(1));\n"
            + "    log(col.namedItem('foo'));\n"
            + "    log(col[1]);\n"
            + "    try {\n"
            + "      log(col(1));\n"
            + "    } catch(e) { logEx(e) }\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"undefined", "undefined", "undefined"})
    public void inexistentProperties() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var x = document.documentElement.childNodes;\n"
            + "    log(x.split);\n"
            + "    log(x.setInterval);\n"
            + "    log(x.bogusNonExistentProperty);\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";
        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"3", "#text", "5"})
    public void childNodes() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    log(document.body.childNodes.length);\n"
            + "    log(document.body.firstChild.nodeName);\n"
            + "    log(document.getElementById('myDiv').childNodes.length);\n"
            + "  }\n"
            + "</script></head>\n"
            + "<body onload='test()'> <div id='myDiv'> <div> </div> <div> </div> </div> </body>\n"
            + "</html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("object")
    public void typeof() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    log(typeof document.getElementsByTagName('a'));\n"
            + "  }\n"
            + "</script></head>\n"
            + "<body onload='test()'></body>\n"
            + "</html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * Verifies that dollar signs don't cause exceptions in {@link HTMLCollection} (which uses Java
     * regex internally). Found via the MooTools unit tests.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"[object HTMLHeadingElement]", "undefined"})
    public void getElementWithDollarSign() throws Exception {
        final String html
            = "<h3 id='$h'>h</h3><script>\n"
            + LOG_TITLE_FUNCTION
            + "var hs = document.getElementsByTagName('h3');\n"
            + "log(hs['$h']);\n"
            + "log(hs['$n']);\n"
            + "</script>";
        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"function", "function", "function", "function"})
    public void array_prototype() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    log(typeof Object.prototype.__defineGetter__);\n"
            + "    log(typeof Object.prototype.__lookupGetter__);\n"
            + "    log(typeof Array.prototype.indexOf);\n"
            + "    log(typeof Array.prototype.map);\n"
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
    @Alerts({"function", "function", "function", "function"})
    public void array_prototype_standards() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    log(typeof Object.prototype.__defineGetter__);\n"
            + "    log(typeof Object.prototype.__lookupGetter__);\n"
            + "    log(typeof Array.prototype.indexOf);\n"
            + "    log(typeof Array.prototype.map);\n"
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
    @Alerts("true")
    public void has() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    log(0 in document.forms);\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "<form name='myForm'></form>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"myForm", "mySecondForm"})
    public void forOf() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    try {"
            + "      for (f of document.forms) {\n"
            + "        log(f.name);\n"
            + "      }\n"
            + "    } catch(e) { logEx(e) }\n"
            + "  }\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "<form name='myForm'></form>\n"
            + "<form name='mySecondForm'></form>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"myForm", "mySecondForm", "dynamicForm", "-", "myForm", "mySecondForm", "dynamicForm"})
    public void forOfDynamicAtEnd() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    try {"
            + "      var i = 0;\n"
            + "      for (f of document.forms) {\n"
            + "        i++;\n"
            + "        if (i == 1) {\n"
            + "          var frm = document.createElement('FORM');\n"
            + "          frm.name = 'dynamicForm';\n"
            + "          document.body.appendChild(frm);\n"
            + "        }\n"
            + "        log(f.name);\n"
            + "      }\n"

            + "      log('-');\n"
            + "      for (f of document.forms) {\n"
            + "        log(f.name);\n"
            + "      }\n"
            + "    } catch(e) { logEx(e) }\n"
            + "  }\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "<form name='myForm'></form>\n"
            + "<form name='mySecondForm'></form>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"myForm", "myForm", "mySecondForm", "-", "dynamicForm", "myForm", "mySecondForm"})
    public void forOfDynamicAtStart() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    try{\n"
            + "      var i = 0;\n"
            + "      for (f of document.forms) {\n"
            + "        i++;\n"
            + "        if (i == 1) {\n"
            + "          var frm = document.createElement('FORM');\n"
            + "          frm.name = 'dynamicForm';\n"
            + "          document.body.insertBefore(frm, document.getElementsByName('myForm')[0]);\n"
            + "        }\n"
            + "        log(f.name);\n"
            + "      }\n"

            + "      log('-');\n"
            + "      for (f of document.forms) {\n"
            + "        log(f.name);\n"
            + "      }\n"
            + "    } catch(e) { logEx(e) }\n"
            + "  }\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "<form name='myForm'></form>\n"
            + "<form name='mySecondForm'></form>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("b1-button1")
    public void item_Unknown() throws Exception {
        item("'foo'");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("b1-button1")
    public void item_ById() throws Exception {
        item("'b2'");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("b1-button1")
    public void item_ByName() throws Exception {
        item("'button2'");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("null")
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
    @Alerts("b2-button2")
    public void item_IndexAsString() throws Exception {
        item("'1'");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("b2-button2")
    public void item_IndexDoubleAsString() throws Exception {
        item("'1.1'");
    }

    private void item(final String name) throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function report(result) {\n"
            + "    if (result == null || result == undefined) {\n"
            + "      log(result);\n"
            + "    } else if (('length' in result) && ('item' in result)) {\n"
            + "      log('coll ' + result.length);\n"
            + "      for(var i = 0; i < result.length; i++) {\n"
            + "        log(result.item(i).id + '-' + result.item(i).name);\n"
            + "      }\n"
            + "    } else if (result.id || result.name) {\n"
            + "      log(result.id + '-' + result.name);\n"
            + "    } else {\n"
            + "      log(result);\n"
            + "    }\n"
            + "  }\n"

            + "  function doTest() {\n"
            + "    try {\n"
            + "      var col = document.getElementsByTagName('button');\n"
            + "      report(col.item(" + name + "));\n"
            + "    } catch(e) { logEx(e); }\n"
            + "  }\n"
            + "</script></head>\n"
            + "<body onload='doTest()'>\n"
            + "  <button id='b1' name='button1'></button>\n"
            + "  <button id='b2' name='button2'></button>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
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
    @Alerts("undefined")
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
        final String html = DOCTYPE_HTML
            + "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function report(result) {\n"
            + "    if (result == null || result == undefined) {\n"
            + "      log(result);\n"
            + "    } else if (('length' in result) && ('item' in result)) {\n"
            + "      log('coll ' + result.length);\n"
            + "      for(var i = 0; i < result.length; i++) {\n"
            + "        log(result.item(i).id + '-' + result.item(i).name);\n"
            + "      }\n"
            + "    } else if (result.id || result.name) {\n"
            + "      log(result.id + '-' + result.name);\n"
            + "    } else {\n"
            + "      log(result);\n"
            + "    }\n"
            + "  }\n"

            + "  function doTest() {\n"
            + "    try {\n"
            + "      var col = document.getElementsByTagName('button');\n"
            + "      report(col[" + name + "]);\n"
            + "    } catch(e) { logEx(e); }\n"
            + "  }\n"
            + "</script></head>\n"
            + "<body onload='doTest()'>\n"
            + "  <button id='b1' name='button1'></button>\n"
            + "  <button id='b2' name='button2'></button>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("TypeError")
    public void functionIndex_Unknown() throws Exception {
        functionIndex("'foo'");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("TypeError")
    public void functionIndex_ById() throws Exception {
        functionIndex("'b2'");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("TypeError")
    public void functionIndex_ByName() throws Exception {
        functionIndex("'button2'");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("TypeError")
    public void functionIndex_NegativIndex() throws Exception {
        functionIndex("-1");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("TypeError")
    public void functionIndex_ZeroIndex() throws Exception {
        functionIndex("0");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("TypeError")
    public void functionIndex_ValidIndex() throws Exception {
        functionIndex("1");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("TypeError")
    public void functionIndex_DoubleIndex() throws Exception {
        functionIndex("1.1");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("TypeError")
    public void functionIndex_InvalidIndex() throws Exception {
        functionIndex("2");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("TypeError")
    public void functionIndex_IndexAsString() throws Exception {
        functionIndex("'2'");
    }

    private void functionIndex(final String name) throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function report(result) {\n"
            + "    if (result == null || result == undefined) {\n"
            + "      log(result);\n"
            + "    } else if (('length' in result) && ('item' in result)) {\n"
            + "      log('coll ' + result.length);\n"
            + "      for(var i = 0; i < result.length; i++) {\n"
            + "        log(result.item(i).id + '-' + result.item(i).name);\n"
            + "      }\n"
            + "    } else if (result.id || result.name) {\n"
            + "      log(result.id + '-' + result.name);\n"
            + "    } else {\n"
            + "      log(result);\n"
            + "    }\n"
            + "  }\n"

            + "  function doTest() {\n"
            + "    try {\n"
            + "      var col = document.getElementsByTagName('button');\n"
            + "      report(col(" + name + "));\n"
            + "    } catch(e) { logEx(e); }\n"
            + "  }\n"
            + "</script></head>\n"
            + "<body onload='doTest()'>\n"
            + "  <button id='b1' name='button1'></button>\n"
            + "  <button id='b2' name='button2'></button>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
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
            CHROME = "button6-button6_2",
            EDGE = "button6-button6_2")
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
    @Alerts("b2-button2")
    public void namedItem_IndexAsString() throws Exception {
        item("'1'");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("b2-button2")
    public void namedItem_IndexDoubleAsString() throws Exception {
        item("'1.1'");
    }

    private void namedItem(final String name) throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function report(result) {\n"
            + "    if (result == null || result == undefined) {\n"
            + "      log(result);\n"
            + "    } else if (result.id || result.name) {\n"
            + "      log(result.id + '-' + result.name);\n"
            + "    } else {\n"
            + "      log('coll ' + result.length);\n"
            + "      for(var i = 0; i < result.length; i++) {\n"
            + "        log(result.item(i).id + '-' + result.item(i).name);\n"
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

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"1", "1"})
    public void setLength() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html>\n"
            + "<head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "function test() {\n"
            + "  var x = document.children;\n"
            + "  try {\n"
            + "    log(x.length);\n"
            + "    x.length = 100;\n"
            + "    log(x.length);\n"
            + "  } catch(e) { log('Type error'); }\n"
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
    @Alerts({"1", "Type error"})
    public void setLengthStrictMode() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html>\n"
            + "<head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "function test() {\n"
            + "  'use strict';\n"
            + "  var x = document.children;\n"
            + "  try {\n"
            + "    log(x.length);\n"
            + "    x.length = 100;\n"
            + "    log(x.length);\n"
            + "  } catch(e) { log('Type error'); }\n"
            + "}\n"
            + "</script></head>\n"
            + "<body onload='test()'>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * See {@link HTMLAllCollectionTest#looselyEqualToUndefined}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"false", "false", "false", "false"})
    public void looselyEqualToUndefined() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html>\n"
            + "<body>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  log(undefined === document.children);\n"
            + "  log(undefined == document.children);\n"
            + "  log(document.children === undefined);\n"
            + "  log(document.children == undefined);\n"
            + "</script>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * See {@link HTMLAllCollectionTest#looselyEqualToNull()}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"false", "false", "false", "false"})
    public void looselyEqualToNull() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html>\n"
            + "<body>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  log(null === document.children);\n"
            + "  log(null == document.children);\n"
            + "  log(document.children === null);\n"
            + "  log(document.children == null);\n"
            + "</script>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * See {@link HTMLAllCollectionTest#falsyInBooleanContexts()}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"1", "7", "4", "[object HTMLCollection]", "1"})
    public void falsyInBooleanContexts() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html>\n"
            + "<body>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  x = 11;\n"
            + "  if(document.children) { x = 1 } else { x = 7 }"
            + "  log(x);\n"

            + "  if(!document.children) { x = 1 } else { x = 7 }"
            + "  log(x);\n"

            + "  log(document.children ? 4 : 3);\n"

            + "  log(document.children ?? 'htmlunit');\n"
            + "  log(document.children?.length);\n"
            + "</script>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }
}
