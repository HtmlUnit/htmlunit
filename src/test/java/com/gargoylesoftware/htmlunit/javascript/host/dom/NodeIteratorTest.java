/*
 * Copyright (c) 2002-2016 Gargoyle Software Inc.
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
    @Alerts("[object HTMLDivElement], [object HTMLSpanElement], [object HTMLSpanElement],"
                + " [object HTMLSpanElement]")
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
            + "<body onload='test()'>"
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
            + "<body onload='test()'>"
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
            + "<body onload='test()'>"
            + "<div id='myId'><span>abc</span><p>def</p><span>ghi</span></div>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }
}
