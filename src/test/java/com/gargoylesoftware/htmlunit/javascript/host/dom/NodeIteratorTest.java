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
import com.gargoylesoftware.htmlunit.WebDriverTestCase;

/**
 * Tests for {@link NodeIterator}.
 *
 * @author Ahmed Ashour
 */
@RunWith(BrowserRunner.class)
public class NodeIteratorTest extends WebDriverTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"[object HTMLDivElement]", "[object HTMLSpanElement]", "[object HTMLSpanElement]",
            "[object HTMLSpanElement]"})
    public void filterNull() throws Exception {
        final String html
            = "<html>\n"
            + "<head>\n"
            + "  <script>\n"
            + "    function test() {\n"
            + "      if (document.createNodeIterator) {\n"
            + "        var nodeIterator = document.createNodeIterator(\n"
            + "          document.getElementById('myId'),\n"
            + "          NodeFilter.SHOW_ELEMENT,\n"
            + "          null\n"
            + "        );\n"

            + "        var currentNode;\n"
            + "        while (currentNode = nodeIterator.nextNode()) {\n"
            + "          alert(currentNode);\n"
            + "        }\n"
            + "      }\n"
            + "    }\n"
            + "  </script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "<div id='myId'><span>a</span><span>b</span><span>c</span></div>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("[object HTMLParagraphElement]")
    public void filterFunction() throws Exception {
        final String html
            = "<html>\n"
            + "<head>\n"
            + "  <script>\n"
            + "    function test() {\n"
            + "      if (document.createNodeIterator) {\n"
            + "        var nodeIterator = document.createNodeIterator(\n"
            + "          document.getElementById('myId'),\n"
            + "          NodeFilter.SHOW_ELEMENT,\n"
            + "          function(node) {\n"
            + "            return node.nodeName.toLowerCase() === 'p' ? NodeFilter.FILTER_ACCEPT"
            + " : NodeFilter.FILTER_REJECT;\n"
            + "          }\n"
            + "        );\n"

            + "        var currentNode;\n"
            + "        while (currentNode = nodeIterator.nextNode()) {\n"
            + "          alert(currentNode);\n"
            + "        }\n"
            + "      }\n"
            + "    }\n"
            + "  </script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "<div id='myId'><span>a</span><p>b</p><span>c</span></div>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("def")
    public void filterObject() throws Exception {
        final String html
            = "<html>\n"
            + "<head>\n"
            + "  <script>\n"
            + "    function test() {\n"
            + "      if (document.createNodeIterator) {\n"
            + "        var nodeIterator = document.createNodeIterator(\n"
            + "          document.getElementById('myId'),\n"
            + "          NodeFilter.SHOW_TEXT,\n"
            + "          { acceptNode: function(node) {\n"
            + "            if (node.data.indexOf('e') != -1) {\n"
            + "              return NodeFilter.FILTER_ACCEPT;\n"
            + "            }\n"
            + "          } }\n"
            + "        );\n"

            + "        var currentNode;\n"
            + "        while (currentNode = nodeIterator.nextNode()) {\n"
            + "          alert(currentNode.data);\n"
            + "        }\n"
            + "      }\n"
            + "    }\n"
            + "  </script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "<div id='myId'><span>abc</span><p>def</p><span>ghi</span></div>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * Test case for issue 1982.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"1", "11", "12"})
    public void subroot() throws Exception {
        final String html
            = "<html>\n"
            + "<head>\n"
            + "  <script>\n"
            + "    function test() {\n"
            + "      if (document.createNodeIterator) {\n"
            + "        var nodeIterator = document.createNodeIterator(\n"
            + "          document.getElementById('1'),\n"
            + "          NodeFilter.SHOW_ELEMENT );\n"

            + "        var currentNode;\n"
            + "        while (currentNode = nodeIterator.nextNode()) {\n"
            + "          alert(currentNode.id);\n"
            + "        }\n"
            + "      }\n"
            + "    }\n"
            + "  </script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "<div id='before'>before</div>\n"
            + "<table>\n"
            + "  <tr id='1'>\n"
            + "    <td id='11'>11</td>\n"
            + "    <td id='12'>12</td>\n"
            + "  </tr>\n"
            + "  <tr id='2'>\n"
            + "    <td id='21'>21</td>\n"
            + "    <td id='22'>22</td>\n"
            + "  </tr>\n"
            + "</table>\n"
            + "<div id='after'>after</div>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }
}
