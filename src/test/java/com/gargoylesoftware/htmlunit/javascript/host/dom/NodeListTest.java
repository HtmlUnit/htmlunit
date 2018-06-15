/*
 * Copyright (c) 2002-2018 Gargoyle Software Inc.
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
package com.gargoylesoftware.htmlunit.javascript.host.dom;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;
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
    @Alerts("true")
    public void has() throws Exception {
        final String html = "<html><head><title>test</title>\n"
                + "<script>\n"
                + "  function test() {\n"
                + "    alert(0 in document.body.parentNode.childNodes);\n"
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
    @Alerts(DEFAULT = "0,1,2,3,4,5,entries,forEach,item,keys,length,values",
            IE = "0,1,2,3,4,5,item,length",
            EDGE = "0,1,2,3,4,5,item,length")
    public void iterator() throws Exception {
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
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = {"[object HTMLHtmlElement] 0 [object NodeList] undefined",
                "[object HTMLHeadElement] 1 [object NodeList] undefined",
                "[object HTMLTitleElement] 2 [object NodeList] undefined",
                "[object HTMLScriptElement] 3 [object NodeList] undefined",
                "[object HTMLBodyElement] 4 [object NodeList] undefined",
                "[object HTMLDivElement] 5 [object NodeList] undefined"},
            IE = "no forEach",
            EDGE = "no forEach")
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
    @Alerts(DEFAULT = {"done", "value", "object", "0", "[object HTMLHtmlElement]"},
            IE = "not defined",
            EDGE = "not defined")
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
}
