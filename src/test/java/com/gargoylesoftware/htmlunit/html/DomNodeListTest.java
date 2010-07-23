/*
 * Copyright (c) 2002-2010 Gargoyle Software Inc.
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
package com.gargoylesoftware.htmlunit.html;

import java.util.Iterator;
import java.util.ListIterator;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.WebTestCase;

/**
 * Tests for {@link DomNodeList}.
 *
 * @version $Revision$
 * @author <a href="mailto:tom.anderson@univ.oxon.org">Tom Anderson</a>
 */
@RunWith(BrowserRunner.class)
public class DomNodeListTest extends WebTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void getElementsByTagName() throws Exception {
        final String firstContent
            = "<html><head><title>First</title></head>\n"
            + "<body>\n"
            + "<form><input type='button' name='button1' value='pushme'></form>\n"
            + "<div>a</div> <div>b</div> <div>c</div>\n"
            + "</body></html>";

        final HtmlPage page = loadPage(firstContent);

        final DomNodeList<HtmlElement> divs = page.getElementsByTagName("div");

        assertEquals(3, divs.getLength());
        validateDomNodeList(divs);

        final HtmlDivision newDiv = new HtmlDivision(null, HtmlDivision.TAG_NAME, page, null);
        page.getBody().appendChild(newDiv);
        assertEquals(4, divs.getLength());
        validateDomNodeList(divs);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void getChildNodes() throws Exception {
        final String firstContent
            = "<html><head><title>First</title></head>\n"
            + "<body>\n"
            + "<form><input type='button' name='button1' value='pushme'></form>\n"
            + "<div>a</div> <div>b</div> <div>c</div>\n"
            + "</body></html>";

        final HtmlPage page = loadPage(firstContent);
        final DomNodeList<DomNode> bodyChildren = page.getBody().getChildNodes();
        validateDomNodeList(bodyChildren);
    }

    private <E extends DomNode> void validateDomNodeList(final DomNodeList<E> nodes) {
        assertEquals(nodes.getLength(), nodes.size());
        final Iterator<E> nodesIterator = nodes.iterator();
        for (int i = 0; i < nodes.getLength(); i++) {
            assertEquals(nodes.item(i), nodes.get(i));
            assertEquals(nodes.item(i), nodesIterator.next());
            assertEquals(i, nodes.indexOf(nodes.item(i)));
        }
        assertEquals(false, nodesIterator.hasNext());
        final ListIterator<E> nodesListIterator = nodes.listIterator();
        assertEquals(nodes.item(0), nodesListIterator.next());
        assertEquals(nodes.item(1), nodesListIterator.next());
        assertEquals(nodes.item(1), nodesListIterator.previous());
    }
}
