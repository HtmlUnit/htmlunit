/*
 * Copyright (c) 2002-2011 Gargoyle Software Inc.
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

import org.junit.Test;
import org.junit.runner.RunWith;
import org.xml.sax.helpers.AttributesImpl;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.WebTestCase;

/**
 * Tests for {@link DefaultElementFactory}.
 *
 * @version $Revision$
 * @author <a href="mailto:marvin.java@gmail.com">Marcos Vinicius B. de Souza</a>
 * @author Marc Guillemot
 * @author Ahmed Ashour
 * @since 1.2
 */
@RunWith(BrowserRunner.class)
public class DefaultElementFactoryTest extends WebTestCase {
    /**
     * Test that the attribute order is the same as the provided one.
     * @throws Exception if the test fails
     */
    @Test
    public void attributeOrder() throws Exception {
        // Construct the test page.
        final String html = "<html><head><title>test page</title></head>\n"
                + "<body><div>test message</div></body></html>";

        // Load the test page.
        final HtmlPage htmlPage = loadPage(html);

        // Creates the attributes of the 'anchor'.
        final AttributesImpl atts = new AttributesImpl();
        atts.addAttribute(null, "href", "href", null, "http://www.google.com");
        atts.addAttribute(null, "tabindex", "tabindex", null, "2");
        atts.addAttribute(null, "accesskey", "accesskey", null, "F");

        // Access the factory.
        final DefaultElementFactory defaultElementFactory = new DefaultElementFactory();

        // Create a anchor element
        final HtmlAnchor anchor = (HtmlAnchor) defaultElementFactory.createElement(htmlPage, "a", atts);

        verifyAttributes(anchor);
    }

    /**
     * @param anchor the anchor which attributes should be checked
     */
    private void verifyAttributes(final HtmlAnchor anchor) {
        // Get the attributes iterator
        final Iterator<DomAttr> attributeEntriesIterator = anchor.getAttributesMap().values().iterator();

        // Verify if the attributes are in ascending order of name.
        DomAttr htmlAttr = attributeEntriesIterator.next();
        assertEquals("href", htmlAttr.getNodeName());
        assertEquals("http://www.google.com", htmlAttr.getValue());

        htmlAttr = attributeEntriesIterator.next();
        assertEquals("tabindex", htmlAttr.getNodeName());
        assertEquals("2", htmlAttr.getValue());

        htmlAttr = attributeEntriesIterator.next();
        assertEquals("accesskey", htmlAttr.getNodeName());
        assertEquals("F", htmlAttr.getValue());
    }

    /**
     * Test the order of attributes.
     * @throws Exception if the test fails
     */
    @Test
    public void attributeOrderLive() throws Exception {
        final String html = "<html><body>\n"
            + "<a href='http://www.google.com' tabindex='2' accesskey='F'>foo</a>\n"
            + "</body></html>";
        final HtmlPage page = loadPage(html);
        final HtmlAnchor anchor = page.getAnchorByText("foo");

        verifyAttributes(anchor);
    }
}
