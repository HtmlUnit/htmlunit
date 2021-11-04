/*
 * Copyright (c) 2002-2021 Gargoyle Software Inc.
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
package com.gargoylesoftware.htmlunit.javascript.host.dom;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;
import com.gargoylesoftware.htmlunit.BrowserRunner.HtmlUnitNYI;
import com.gargoylesoftware.htmlunit.WebDriverTestCase;
import com.gargoylesoftware.htmlunit.html.HtmlPageTest;

/**
 * Tests for {@link NodeList}.
 *
 * @author Ahmed Ashour
 * @author Ronald Brill
 */
@RunWith(BrowserRunner.class)
public class NodeListTest extends WebDriverTestCase {

    /**
     * @throws Exception on test failure
     */
    @Test
    @Alerts("[object NodeList]")
    public void defaultValue() throws Exception {
        final String html = HtmlPageTest.STANDARDS_MODE_PREFIX_ + "<html><head>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "  function test() {\n"
                + "    log(document.getElementById('myId').firstChild.childNodes);\n"
                + "  }\n"
                + "</script>\n"
                + "</head><body onload='test()'>\n"
                + "  <div id='myId'>abcd</div>\n"
                + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception on test failure
     */
    @Test
    @Alerts(DEFAULT = {"true", "true", "false", "true", "true", "true", "true", "true", "true"},
            IE = {"true", "true", "false", "false", "false", "true", "false", "true", "false"})
    public void has() throws Exception {
        final String html = "<html><head>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "  function test() {\n"
                + "    var nodeList = document.querySelectorAll('*');\n"
                + "    log(0 in nodeList);\n"
                + "    log(3 in nodeList);\n"
                + "    log(4 in nodeList);\n"

                + "    log('entries' in nodeList);\n"
                + "    log('forEach' in nodeList);\n"
                + "    log('item' in nodeList);\n"
                + "    log('keys' in nodeList);\n"
                + "    log('length' in nodeList);\n"
                + "    log('values' in nodeList);\n"
                + "  }\n"
                + "</script>\n"
                + "</head><body onload='test()'>\n"
                + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception on test failure
     */
    @Test
    @Alerts({"4", "true", "undefined"})
    public void length() throws Exception {
        final String html = "<html><head>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "  function test() {\n"
                + "    var nodeList = document.querySelectorAll('*');\n"
                + "    log(nodeList.length);\n"

                + "    log('length' in nodeList);\n"
                + "    log(Object.getOwnPropertyDescriptor(nodeList, 'length'));\n"
                + "  }\n"
                + "</script>\n"
                + "</head><body onload='test()'>\n"
                + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception on test failure
     */
    @Test
    @Alerts({"[object HTMLHtmlElement]", "[object HTMLScriptElement]", "null", "null"})
    public void item() throws Exception {
        final String html = HtmlPageTest.STANDARDS_MODE_PREFIX_ + "<html><head>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "  function test() {\n"
                + "    var nodeList = document.querySelectorAll('*');\n"
                + "    log(nodeList.item(0));\n"
                + "    log(nodeList.item(2));\n"
                + "    log(nodeList.item(17));\n"
                + "    log(nodeList.item(-1));\n"
                + "  }\n"
                + "</script>\n"
                + "</head><body onload='test()'>\n"
                + "  <div></div>\n"
                + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception on test failure
     */
    @Test
    @Alerts({"[object HTMLHtmlElement]", "[object HTMLScriptElement]", "undefined", "undefined"})
    public void itemBracketed() throws Exception {
        final String html = HtmlPageTest.STANDARDS_MODE_PREFIX_ + "<html><head>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "  function test() {\n"
                + "    var nodeList = document.querySelectorAll('*');\n"
                + "    log(nodeList[0]);\n"
                + "    log(nodeList[2]);\n"
                + "    log(nodeList[17]);\n"
                + "    log(nodeList[-1]);\n"
                + "  }\n"
                + "</script>\n"
                + "</head><body onload='test()'>\n"
                + "  <div></div>\n"
                + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception on test failure
     */
    @Test
    @Alerts(DEFAULT = "0,1,2,3,4,entries,forEach,item,keys,length,values",
            IE = "0,1,2,3,4,item,length")
    public void forIn() throws Exception {
        final String html = HtmlPageTest.STANDARDS_MODE_PREFIX_ + "<html><head>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "  function test() {\n"
                + "    var all = [];\n"
                + "    for (var i in document.querySelectorAll('*')) {\n"
                + "      all.push(i);\n"
                + "    }\n"
                + "    all.sort(sortFunction);\n"
                + "    log(all);\n"
                + "  }\n"
                + "  function sortFunction(s1, s2) {\n"
                + "    return s1.toLowerCase() > s2.toLowerCase() ? 1 : -1;\n"
                + "  }\n"
                + "</script>\n"
                + "</head><body onload='test()'>\n"
                + "  <div></div>\n"
                + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception on test failure
     */
    @Test
    @Alerts(DEFAULT = "entries,forEach,item,keys,length,values",
            IE = "item,length")
    public void forInEmptyList() throws Exception {
        final String html = HtmlPageTest.STANDARDS_MODE_PREFIX_ + "<html><head>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "  function test() {\n"
                + "    var all = [];\n"
                + "    for (var i in document.querySelectorAll('.notThere')) {\n"
                + "      all.push(i);\n"
                + "    }\n"
                + "    all.sort(sortFunction);\n"
                + "    log(all);\n"
                + "  }\n"
                + "  function sortFunction(s1, s2) {\n"
                + "    return s1.toLowerCase() > s2.toLowerCase() ? 1 : -1;\n"
                + "  }\n"
                + "</script>\n"
                + "</head><body onload='test()'>\n"
                + "  <div></div>\n"
                + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception on test failure
     */
    @Test
    @Alerts(DEFAULT = {"[object HTMLHtmlElement]", "[object HTMLHeadElement]",
                       "[object HTMLScriptElement]", "[object HTMLBodyElement]",
                       "[object HTMLDivElement]"},
            IE = "no for..of")
    public void iterator() throws Exception {
        final String html = HtmlPageTest.STANDARDS_MODE_PREFIX_ + "<html><head>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "  function test() {\n"
                + "    var nodeList = document.querySelectorAll('*');\n"
                + "    if (!nodeList.forEach) {\n"
                + "      log('no for..of');\n"
                + "      return;\n"
                + "    }\n"

                + "    for (var i of nodeList) {\n"
                + "      log(i);\n"
                + "    }\n"
                + "  }\n"
                + "</script>\n"
                + "</head><body onload='test()'>\n"
                + "  <div></div>\n"
                + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = {"[object HTMLHtmlElement] 0 [object NodeList] undefined",
                       "[object HTMLHeadElement] 1 [object NodeList] undefined",
                       "[object HTMLScriptElement] 2 [object NodeList] undefined",
                       "[object HTMLBodyElement] 3 [object NodeList] undefined",
                       "[object HTMLDivElement] 4 [object NodeList] undefined"},
            IE = "no forEach")
    public void forEach() throws Exception {
        final String html = "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var nodeList = document.querySelectorAll('*');\n"
            + "    if (nodeList.forEach) {\n"
            + "      nodeList.forEach(myFunction, 'something');\n"
            + "    } else {\n"
            + "      log('no forEach');\n"
            + "    }\n"
            + "  }\n"
            + "  function myFunction(value, index, list, arg) {\n"
            + "    log(value + ' ' + index + ' ' + list + ' ' + arg);\n"
            + "  }\n"
            + "</script>\n"
            + "</head><body onload='test()'>\n"
            + "  <div></div>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = {"value", "done", "object", "0", "[object HTMLHtmlElement]"},
            IE = "not defined")
    public void entries() throws Exception {
        final String html = "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var nodeList = document.querySelectorAll('*');\n"
            + "    if (!nodeList.entries) {\n"
            + "      log('not defined');\n"
            + "      return;\n"
            + "    }\n"
            + "    var i = nodeList.entries().next();\n"
            + "    for (var x in i) {\n"
            + "      log(x);\n"
            + "    }\n"
            + "    var v = i.value;\n"
            + "    log(typeof v);\n"
            + "    log(v[0]);\n"
            + "    log(v[1]);\n"
            + "  }\n"
            + "</script>\n"
            + "</head><body onload='test()'>\n"
            + "</body></html>\n";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = {"true", "undefined", "function", "undefined", "undefined", "true", "true", "true"},
            IE = {"false", "undefined", "no entries"})
    public void entriesPropertyDescriptor() throws Exception {
        final String html = "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var nodeList = document.querySelectorAll('*');\n"

            + "    log('entries' in nodeList);\n"
            + "    log(Object.getOwnPropertyDescriptor(nodeList, 'entries'));\n"

            + "    var desc = Object.getOwnPropertyDescriptor(Object.getPrototypeOf(nodeList), 'entries');\n"
            + "    if (desc === undefined) { log('no entries'); return; }\n"
            + "    log(typeof desc.value);\n"
            + "    log(desc.get);\n"
            + "    log(desc.set);\n"
            + "    log(desc.writable);\n"
            + "    log(desc.enumerable);\n"
            + "    log(desc.configurable);\n"
            + "  }\n"
            + "</script>\n"
            + "</head><body onload='test()'>\n"
            + "</body></html>\n";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = {"0,[object HTMLHtmlElement]", "1,[object HTMLHeadElement]",
                       "2,[object HTMLScriptElement]", "3,[object HTMLBodyElement]"},
            IE = {})
    @HtmlUnitNYI(IE = "not defined")
    public void entriesForOf() throws Exception {
        final String html = "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var nodeList = document.querySelectorAll('*');\n"
            + "    if (!nodeList.entries) {\n"
            + "      log('not defined');\n"
            + "      return;\n"
            + "    }\n"
            + "    for (var i of nodeList.entries()) {\n"
            + "      log(i);\n"
            + "    }\n"
            + "  }\n"
            + "</script>\n"
            + "</head><body onload='test()'>\n"
            + "</body></html>\n";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = {"value", "done", "number", "0"},
            IE = "not defined")
    public void keys() throws Exception {
        final String html = "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var nodeList = document.querySelectorAll('*');\n"
            + "    if (!nodeList.keys) {\n"
            + "      log('not defined');\n"
            + "      return;\n"
            + "    }\n"
            + "    var i = nodeList.keys().next();\n"
            + "    for (var x in i) {\n"
            + "      log(x);\n"
            + "    }\n"
            + "    var v = i.value;\n"
            + "    log(typeof v);\n"
            + "    log(v);\n"
            + "  }\n"
            + "</script>\n"
            + "</head><body onload='test()'>\n"
            + "</body></html>\n";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = {"true", "undefined", "function", "undefined", "undefined", "true", "true", "true"},
            IE = {"false", "undefined", "no keys"})
    public void keysPropertyDescriptor() throws Exception {
        final String html = "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var nodeList = document.querySelectorAll('*');\n"

            + "    log('keys' in nodeList);\n"
            + "    log(Object.getOwnPropertyDescriptor(nodeList, 'keys'));\n"

            + "    var desc = Object.getOwnPropertyDescriptor(Object.getPrototypeOf(nodeList), 'keys');\n"
            + "    if (desc === undefined) { log('no keys'); return; }\n"
            + "    log(typeof desc.value);\n"
            + "    log(desc.get);\n"
            + "    log(desc.set);\n"
            + "    log(desc.writable);\n"
            + "    log(desc.enumerable);\n"
            + "    log(desc.configurable);\n"
            + "  }\n"
            + "</script>\n"
            + "</head><body onload='test()'>\n"
            + "</body></html>\n";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = {"0", "1", "2", "3"},
            IE = {})
    @HtmlUnitNYI(IE = "not defined")
    public void keysForOf() throws Exception {
        final String html = "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var nodeList = document.querySelectorAll('*');\n"
            + "    if (!nodeList.keys) {\n"
            + "      log('not defined');\n"
            + "      return;\n"
            + "    }\n"
            + "    for (var i of nodeList.keys()) {\n"
            + "      log(i);\n"
            + "    }\n"
            + "  }\n"
            + "</script>\n"
            + "</head><body onload='test()'>\n"
            + "</body></html>\n";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"0,1,2,3", ""})
    public void objectKeys() throws Exception {
        final String html = "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var nodeList = document.querySelectorAll('*');\n"
            + "    log(Object.keys(nodeList));\n"

            + "    nodeList = document.querySelectorAll('.notThere');\n"
            + "    log(Object.keys(nodeList));\n"
            + "  }\n"
            + "</script>\n"
            + "</head><body onload='test()'>\n"
            + "</body></html>\n";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = {"value", "done", "object", "[object HTMLHtmlElement]"},
            IE = "not defined")
    public void values() throws Exception {
        final String html = "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var nodeList = document.querySelectorAll('*');\n"
            + "    if (!nodeList.values) {\n"
            + "      log('not defined');\n"
            + "      return;\n"
            + "    }\n"
            + "    var i = nodeList.values().next();\n"
            + "    for (var x in i) {\n"
            + "      log(x);\n"
            + "    }\n"
            + "    var v = i.value;\n"
            + "    log(typeof v);\n"
            + "    log(v);\n"
            + "  }\n"
            + "</script>\n"
            + "</head><body onload='test()'>\n"
            + "</body></html>\n";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = {"true", "undefined", "function", "undefined", "undefined", "true", "true", "true"},
            IE = {"false", "undefined", "no values"})
    public void valuesPropertyDescriptor() throws Exception {
        final String html = "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var nodeList = document.querySelectorAll('*');\n"

            + "    log('values' in nodeList);\n"
            + "    log(Object.getOwnPropertyDescriptor(nodeList, 'values'));\n"

            + "    var desc = Object.getOwnPropertyDescriptor(Object.getPrototypeOf(nodeList), 'values');\n"
            + "    if (desc === undefined) { log('no values'); return; }\n"
            + "    log(typeof desc.value);\n"
            + "    log(desc.get);\n"
            + "    log(desc.set);\n"
            + "    log(desc.writable);\n"
            + "    log(desc.enumerable);\n"
            + "    log(desc.configurable);\n"
            + "  }\n"
            + "</script>\n"
            + "</head><body onload='test()'>\n"
            + "</body></html>\n";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = {"[object HTMLHtmlElement]", "[object HTMLHeadElement]",
                       "[object HTMLScriptElement]", "[object HTMLBodyElement]"},
            IE = {})
    @HtmlUnitNYI(IE = "not defined")
    public void valuesForOf() throws Exception {
        final String html = "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var nodeList = document.querySelectorAll('*');\n"
            + "    if (!nodeList.values) {\n"
            + "      log('not defined');\n"
            + "      return;\n"
            + "    }\n"
            + "    for (var i of nodeList.values()) {\n"
            + "      log(i);\n"
            + "    }\n"
            + "  }\n"
            + "</script>\n"
            + "</head><body onload='test()'>\n"
            + "</body></html>\n";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception on failure
     */
    @Test
    @Alerts(DEFAULT = {"0", "4", "0", "1", "2", "3"},
            IE = "not defined")
    public void getOwnPropertySymbols() throws Exception {
        final String html = "<html><body>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "  if (Object.getOwnPropertySymbols) {\n"

                + "    var nodeList = document.querySelectorAll('*');\n"

                + "    var objectSymbols = Object.getOwnPropertySymbols(nodeList);\n"
                + "    log(objectSymbols.length);\n"

                + "    var objectNames = Object.getOwnPropertyNames(nodeList);\n"
                + "    log(objectNames.length);\n"
                + "    log(objectNames[0]);\n"
                + "    log(objectNames[1]);\n"
                + "    log(objectNames[2]);\n"
                + "    log(objectNames[3]);\n"

                + "  } else { log('not defined'); }\n"
                + "</script>\n"
                + "</body>\n"
                + "</html>\n";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception on failure
     */
    @Test
    @Alerts(DEFAULT = {"0", "0"},
            IE = "not defined")
    public void getOwnPropertySymbolsEmptyList() throws Exception {
        final String html = "<html><body>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "  if (Object.getOwnPropertySymbols) {\n"

                + "    var nodeList = document.querySelectorAll('.notThere');\n"

                + "    var objectSymbols = Object.getOwnPropertySymbols(nodeList);\n"
                + "    log(objectSymbols.length);\n"

                + "    var objectNames = Object.getOwnPropertyNames(nodeList);\n"
                + "    log(objectNames.length);\n"

                + "  } else { log('not defined'); }\n"
                + "</script>\n"
                + "</body>\n"
                + "</html>\n";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"[object HTMLHeadElement]", "[object HTMLHeadElement]"})
    public void setItem() throws Exception {
        final String html = "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var nodeList = document.querySelectorAll('*');\n"
            + "    log(nodeList.item(1));\n"
            + "    nodeList[1] = nodeList.item(0);\n"
            + "    log(nodeList.item(1));\n"
            + "  }\n"
            + "</script>\n"
            + "</head><body onload='test()'>\n"
            + "</body></html>\n";

        loadPageVerifyTitle2(html);
    }
}
