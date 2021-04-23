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
package com.gargoylesoftware.htmlunit.html;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.w3c.dom.traversal.NodeFilter;
import org.w3c.dom.traversal.NodeIterator;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.SimpleWebTestCase;

/**
 * Tests for {@link DomNodeIterator}.
 *
 * @author Ahmed Ashour
 * @author Ronald Brill
 */
@RunWith(BrowserRunner.class)
public final class DomNodeIterator2Test extends SimpleWebTestCase {

    /**
     * Test case for issue 1982.
     * @throws Exception if the test fails
     */
    @Test
    public void subroot() throws Exception {
        final String html
            = "<html>\n"
            + "<head>\n"
            + "</head>\n"
            + "<body>\n"
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
            + "</body></html>";

        final HtmlPage page = loadPage(html);

        final HtmlElement subroot = (HtmlElement) page.getElementById("1");
        final NodeIterator iterator = page.createNodeIterator(subroot, NodeFilter.SHOW_ELEMENT, null, true);

        HtmlElement element = (HtmlElement) iterator.nextNode();
        Assert.assertEquals("1", element.getId());

        element = (HtmlElement) iterator.nextNode();
        Assert.assertEquals("11", element.getId());

        element = (HtmlElement) iterator.nextNode();
        Assert.assertEquals("12", element.getId());

        Assert.assertNull(iterator.nextNode());
    }
}
