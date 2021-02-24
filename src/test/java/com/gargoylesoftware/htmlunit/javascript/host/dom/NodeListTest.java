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
        final String html = HtmlPageTest.STANDARDS_MODE_PREFIX_ + "<html><head><title>test</title>\n"
                + "<script>\n"
                + "  function test() {\n"
                + "    alert(document.getElementById('myId').firstChild.childNodes);\n"
                + "  }\n"
                + "</script>\n"
                + "</head><body onload='test()'>\n"
                + "  <div id='myId'>abcd</div>\n"
                + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception on test failure
     */
    @Test
    @Alerts(DEFAULT = {"true", "true", "false", "true", "true", "true", "true", "true", "true"},
            IE = {"true", "true", "false", "false", "false", "true", "false", "true", "false"})
    public void has() throws Exception {
        final String html = "<html><head><title>test</title>\n"
                + "<script>\n"
                + "  function test() {\n"
                + "    var nodeList = document.querySelectorAll('*');\n"
                + "    alert(0 in nodeList);\n"
                + "    alert(4 in nodeList);\n"
                + "    alert(5 in nodeList);\n"

                + "    alert('entries' in nodeList);\n"
                + "    alert('forEach' in nodeList);\n"
                + "    alert('item' in nodeList);\n"
                + "    alert('keys' in nodeList);\n"
                + "    alert('length' in nodeList);\n"
                + "    alert('values' in nodeList);\n"
                + "  }\n"
                + "</script>\n"
                + "</head><body onload='test()'>\n"
                + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception on test failure
     */
    @Test
    @Alerts({"5", "true", "undefined"})
    public void length() throws Exception {
        final String html = "<html><head><title>test</title>\n"
                + "<script>\n"
                + "  function test() {\n"
                + "    var nodeList = document.querySelectorAll('*');\n"
                + "    alert(nodeList.length);\n"

                + "    alert('length' in nodeList);\n"
                + "    alert(Object.getOwnPropertyDescriptor(nodeList, 'length'));\n"
                + "  }\n"
                + "</script>\n"
                + "</head><body onload='test()'>\n"
                + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception on test failure
     */
    @Test
    @Alerts({"[object HTMLHtmlElement]", "[object HTMLTitleElement]", "null", "null"})
    public void item() throws Exception {
        final String html = HtmlPageTest.STANDARDS_MODE_PREFIX_ + "<html><head><title>test</title>\n"
                + "<script>\n"
                + "  function test() {\n"
                + "    var nodeList = document.querySelectorAll('*');\n"
                + "    alert(nodeList.item(0));\n"
                + "    alert(nodeList.item(2));\n"
                + "    alert(nodeList.item(17));\n"
                + "    alert(nodeList.item(-1));\n"
                + "  }\n"
                + "</script>\n"
                + "</head><body onload='test()'>\n"
                + "  <div></div>\n"
                + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception on test failure
     */
    @Test
    @Alerts({"[object HTMLHtmlElement]", "[object HTMLTitleElement]", "undefined", "undefined"})
    public void itemBracketed() throws Exception {
        final String html = HtmlPageTest.STANDARDS_MODE_PREFIX_ + "<html><head><title>test</title>\n"
                + "<script>\n"
                + "  function test() {\n"
                + "    var nodeList = document.querySelectorAll('*');\n"
                + "    alert(nodeList[0]);\n"
                + "    alert(nodeList[2]);\n"
                + "    alert(nodeList[17]);\n"
                + "    alert(nodeList[-1]);\n"
                + "  }\n"
                + "</script>\n"
                + "</head><body onload='test()'>\n"
                + "  <div></div>\n"
                + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception on test failure
     */
    @Test
    @Alerts(DEFAULT = "0,1,2,3,4,5,entries,forEach,item,keys,length,values",
            IE = "0,1,2,3,4,5,item,length")
    public void forIn() throws Exception {
        final String html = HtmlPageTest.STANDARDS_MODE_PREFIX_ + "<html><head><title>test</title>\n"
                + "<script>\n"
                + "  function test() {\n"
                + "    var all = [];\n"
                + "    for (var i in document.querySelectorAll('*')) {\n"
                + "      all.push(i);\n"
                + "    }\n"
                + "    all.sort(sortFunction);\n"
                + "    alert(all);\n"
                + "  }\n"
                + "  function sortFunction(s1, s2) {\n"
                + "    return s1.toLowerCase() > s2.toLowerCase() ? 1 : -1;\n"
                + "  }\n"
                + "</script>\n"
                + "</head><body onload='test()'>\n"
                + "  <div></div>\n"
                + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception on test failure
     */
    @Test
    @Alerts(DEFAULT = "entries,forEach,item,keys,length,values",
            IE = "item,length")
    public void forInEmptyList() throws Exception {
        final String html = HtmlPageTest.STANDARDS_MODE_PREFIX_ + "<html><head><title>test</title>\n"
                + "<script>\n"
                + "  function test() {\n"
                + "    var all = [];\n"
                + "    for (var i in document.querySelectorAll('.notThere')) {\n"
                + "      all.push(i);\n"
                + "    }\n"
                + "    all.sort(sortFunction);\n"
                + "    alert(all);\n"
                + "  }\n"
                + "  function sortFunction(s1, s2) {\n"
                + "    return s1.toLowerCase() > s2.toLowerCase() ? 1 : -1;\n"
                + "  }\n"
                + "</script>\n"
                + "</head><body onload='test()'>\n"
                + "  <div></div>\n"
                + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception on test failure
     */
    @Test
    @Alerts(DEFAULT = {"[object HTMLHtmlElement]", "[object HTMLHeadElement]",
                "[object HTMLTitleElement]", "[object HTMLScriptElement]",
                "[object HTMLBodyElement]", "[object HTMLDivElement]"},
            IE = "no for..of")
    public void iterator() throws Exception {
        final String html = HtmlPageTest.STANDARDS_MODE_PREFIX_ + "<html><head><title>test</title>\n"
                + "<script>\n"
                + "  function test() {\n"
                + "    var nodeList = document.querySelectorAll('*');\n"
                + "    if (!nodeList.forEach) {\n"
                + "      alert('no for..of');\n"
                + "      return;\n"
                + "    }\n"

                + "    for (var i of nodeList) {\n"
                + "      alert(i);\n"
                + "    }\n"
                + "  }\n"
                + "</script>\n"
                + "</head><body onload='test()'>\n"
                + "  <div></div>\n"
                + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = {"[object HTMLHtmlElement] 0 [object NodeList] undefined",
                "[object HTMLHeadElement] 1 [object NodeList] undefined",
                "[object HTMLTitleElement] 2 [object NodeList] undefined",
                "[object HTMLScriptElement] 3 [object NodeList] undefined",
                "[object HTMLBodyElement] 4 [object NodeList] undefined",
                "[object HTMLDivElement] 5 [object NodeList] undefined"},
            IE = "no forEach")
    public void forEach() throws Exception {
        final String html = "<html><head><title>test</title>\n"
            + "<script>\n"
            + "  function test() {\n"
            + "    var nodeList = document.querySelectorAll('*');\n"
            + "    if (nodeList.forEach) {\n"
            + "      nodeList.forEach(myFunction, 'something');\n"
            + "    } else {\n"
            + "      alert('no forEach');\n"
            + "    }\n"
            + "  }\n"
            + "  function myFunction(value, index, list, arg) {\n"
            + "    alert(value + ' ' + index + ' ' + list + ' ' + arg);\n"
            + "  }\n"
            + "</script>\n"
            + "</head><body onload='test()'>\n"
            + "  <div></div>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = {"value", "done", "object", "0", "[object HTMLHtmlElement]"},
            IE = "not defined")
    public void entries() throws Exception {
        final String html = "<html><head><title>test</title>\n"
            + "<script>\n"
            + "  function test() {\n"
            + "    var nodeList = document.querySelectorAll('*');\n"
            + "    if (!nodeList.entries) {\n"
            + "      alert('not defined');\n"
            + "      return;\n"
            + "    }\n"
            + "    var i = nodeList.entries().next();\n"
            + "    for (var x in i) {\n"
            + "      alert(x);\n"
            + "    }\n"
            + "    var v = i.value;\n"
            + "    alert(typeof v);\n"
            + "    alert(v[0]);\n"
            + "    alert(v[1]);\n"
            + "  }\n"
            + "</script>\n"
            + "</head><body onload='test()'>\n"
            + "</body></html>\n";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = {"true", "undefined", "function", "undefined", "undefined", "true", "true", "true"},
            IE = {"false", "undefined", "no entries"})
    public void entriesPropertyDescriptor() throws Exception {
        final String html = "<html><head><title>test</title>\n"
            + "<script>\n"
            + "  function test() {\n"
            + "    var nodeList = document.querySelectorAll('*');\n"

            + "    alert('entries' in nodeList);\n"
            + "    alert(Object.getOwnPropertyDescriptor(nodeList, 'entries'));\n"

            + "    var desc = Object.getOwnPropertyDescriptor(Object.getPrototypeOf(nodeList), 'entries');\n"
            + "    if (desc === undefined) { alert('no entries'); return; }\n"
            + "    alert(typeof desc.value);\n"
            + "    alert(desc.get);\n"
            + "    alert(desc.set);\n"
            + "    alert(desc.writable);\n"
            + "    alert(desc.enumerable);\n"
            + "    alert(desc.configurable);\n"
            + "  }\n"
            + "</script>\n"
            + "</head><body onload='test()'>\n"
            + "</body></html>\n";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = {"0,[object HTMLHtmlElement]", "1,[object HTMLHeadElement]",
            "2,[object HTMLTitleElement]", "3,[object HTMLScriptElement]",
            "4,[object HTMLBodyElement]"},
            IE = {})
    @HtmlUnitNYI(IE = "not defined")
    public void entriesForOf() throws Exception {
        final String html = "<html><head><title>test</title>\n"
            + "<script>\n"
            + "  function test() {\n"
            + "    var nodeList = document.querySelectorAll('*');\n"
            + "    if (!nodeList.entries) {\n"
            + "      alert('not defined');\n"
            + "      return;\n"
            + "    }\n"
            + "    for (var i of nodeList.entries()) {\n"
            + "      alert(i);\n"
            + "    }\n"
            + "  }\n"
            + "</script>\n"
            + "</head><body onload='test()'>\n"
            + "</body></html>\n";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = {"value", "done", "number", "0"},
            IE = "not defined")
    public void keys() throws Exception {
        final String html = "<html><head><title>test</title>\n"
            + "<script>\n"
            + "  function test() {\n"
            + "    var nodeList = document.querySelectorAll('*');\n"
            + "    if (!nodeList.keys) {\n"
            + "      alert('not defined');\n"
            + "      return;\n"
            + "    }\n"
            + "    var i = nodeList.keys().next();\n"
            + "    for (var x in i) {\n"
            + "      alert(x);\n"
            + "    }\n"
            + "    var v = i.value;\n"
            + "    alert(typeof v);\n"
            + "    alert(v);\n"
            + "  }\n"
            + "</script>\n"
            + "</head><body onload='test()'>\n"
            + "</body></html>\n";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = {"true", "undefined", "function", "undefined", "undefined", "true", "true", "true"},
            IE = {"false", "undefined", "no keys"})
    public void keysPropertyDescriptor() throws Exception {
        final String html = "<html><head><title>test</title>\n"
            + "<script>\n"
            + "  function test() {\n"
            + "    var nodeList = document.querySelectorAll('*');\n"

            + "    alert('keys' in nodeList);\n"
            + "    alert(Object.getOwnPropertyDescriptor(nodeList, 'keys'));\n"

            + "    var desc = Object.getOwnPropertyDescriptor(Object.getPrototypeOf(nodeList), 'keys');\n"
            + "    if (desc === undefined) { alert('no keys'); return; }\n"
            + "    alert(typeof desc.value);\n"
            + "    alert(desc.get);\n"
            + "    alert(desc.set);\n"
            + "    alert(desc.writable);\n"
            + "    alert(desc.enumerable);\n"
            + "    alert(desc.configurable);\n"
            + "  }\n"
            + "</script>\n"
            + "</head><body onload='test()'>\n"
            + "</body></html>\n";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = {"0", "1", "2", "3", "4"},
            IE = {})
    @HtmlUnitNYI(IE = "not defined")
    public void keysForOf() throws Exception {
        final String html = "<html><head><title>test</title>\n"
            + "<script>\n"
            + "  function test() {\n"
            + "    var nodeList = document.querySelectorAll('*');\n"
            + "    if (!nodeList.keys) {\n"
            + "      alert('not defined');\n"
            + "      return;\n"
            + "    }\n"
            + "    for (var i of nodeList.keys()) {\n"
            + "      alert(i);\n"
            + "    }\n"
            + "  }\n"
            + "</script>\n"
            + "</head><body onload='test()'>\n"
            + "</body></html>\n";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = {"0,1,2,3,4", ""},
            IE = {})
    @HtmlUnitNYI(IE = {"0,1,2,3,4", ""})
    public void objectKeys() throws Exception {
        final String html = "<html><head><title>test</title>\n"
            + "<script>\n"
            + "  function test() {\n"
            + "    var nodeList = document.querySelectorAll('*');\n"
            + "    alert(Object.keys(nodeList));\n"

            + "    nodeList = document.querySelectorAll('.notThere');\n"
            + "    alert(Object.keys(nodeList));\n"
            + "  }\n"
            + "</script>\n"
            + "</head><body onload='test()'>\n"
            + "</body></html>\n";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = {"value", "done", "object", "[object HTMLHtmlElement]"},
            IE = "not defined")
    public void values() throws Exception {
        final String html = "<html><head><title>test</title>\n"
            + "<script>\n"
            + "  function test() {\n"
            + "    var nodeList = document.querySelectorAll('*');\n"
            + "    if (!nodeList.values) {\n"
            + "      alert('not defined');\n"
            + "      return;\n"
            + "    }\n"
            + "    var i = nodeList.values().next();\n"
            + "    for (var x in i) {\n"
            + "      alert(x);\n"
            + "    }\n"
            + "    var v = i.value;\n"
            + "    alert(typeof v);\n"
            + "    alert(v);\n"
            + "  }\n"
            + "</script>\n"
            + "</head><body onload='test()'>\n"
            + "</body></html>\n";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = {"true", "undefined", "function", "undefined", "undefined", "true", "true", "true"},
            IE = {"false", "undefined", "no values"})
    public void valuesPropertyDescriptor() throws Exception {
        final String html = "<html><head><title>test</title>\n"
            + "<script>\n"
            + "  function test() {\n"
            + "    var nodeList = document.querySelectorAll('*');\n"

            + "    alert('values' in nodeList);\n"
            + "    alert(Object.getOwnPropertyDescriptor(nodeList, 'values'));\n"

            + "    var desc = Object.getOwnPropertyDescriptor(Object.getPrototypeOf(nodeList), 'values');\n"
            + "    if (desc === undefined) { alert('no values'); return; }\n"
            + "    alert(typeof desc.value);\n"
            + "    alert(desc.get);\n"
            + "    alert(desc.set);\n"
            + "    alert(desc.writable);\n"
            + "    alert(desc.enumerable);\n"
            + "    alert(desc.configurable);\n"
            + "  }\n"
            + "</script>\n"
            + "</head><body onload='test()'>\n"
            + "</body></html>\n";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = {"[object HTMLHtmlElement]", "[object HTMLHeadElement]",
            "[object HTMLTitleElement]", "[object HTMLScriptElement]",
            "[object HTMLBodyElement]"},
            IE = {})
    @HtmlUnitNYI(IE = "not defined")
    public void valuesForOf() throws Exception {
        final String html = "<html><head><title>test</title>\n"
            + "<script>\n"
            + "  function test() {\n"
            + "    var nodeList = document.querySelectorAll('*');\n"
            + "    if (!nodeList.values) {\n"
            + "      alert('not defined');\n"
            + "      return;\n"
            + "    }\n"
            + "    for (var i of nodeList.values()) {\n"
            + "      alert(i);\n"
            + "    }\n"
            + "  }\n"
            + "</script>\n"
            + "</head><body onload='test()'>\n"
            + "</body></html>\n";

        loadPageWithAlerts2(html);
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
                + "  if (Object.getOwnPropertySymbols) {\n"

                + "    var nodeList = document.querySelectorAll('*');\n"

                + "    var objectSymbols = Object.getOwnPropertySymbols(nodeList);\n"
                + "    alert(objectSymbols.length);\n"

                + "    var objectNames = Object.getOwnPropertyNames(nodeList);\n"
                + "    alert(objectNames.length);\n"
                + "    alert(objectNames[0]);\n"
                + "    alert(objectNames[1]);\n"
                + "    alert(objectNames[2]);\n"
                + "    alert(objectNames[3]);\n"

                + "  } else { alert('not defined'); }\n"
                + "</script>\n"
                + "</body>\n"
                + "</html>\n";

        loadPageWithAlerts2(html);
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
                + "  if (Object.getOwnPropertySymbols) {\n"

                + "    var nodeList = document.querySelectorAll('.notThere');\n"

                + "    var objectSymbols = Object.getOwnPropertySymbols(nodeList);\n"
                + "    alert(objectSymbols.length);\n"

                + "    var objectNames = Object.getOwnPropertyNames(nodeList);\n"
                + "    alert(objectNames.length);\n"

                + "  } else { alert('not defined'); }\n"
                + "</script>\n"
                + "</body>\n"
                + "</html>\n";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"[object HTMLHeadElement]", "[object HTMLHeadElement]"})
    public void setItem() throws Exception {
        final String html = "<html><head><title>test</title>\n"
            + "<script>\n"
            + "  function test() {\n"
            + "    var nodeList = document.querySelectorAll('*');\n"
            + "    alert(nodeList.item(1));\n"
            + "    nodeList[1] = nodeList.item(0);\n"
            + "    alert(nodeList.item(1));\n"
            + "  }\n"
            + "</script>\n"
            + "</head><body onload='test()'>\n"
            + "</body></html>\n";

        loadPageWithAlerts2(html);
    }
}
