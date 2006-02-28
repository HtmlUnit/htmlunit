/*
 * Copyright (c) 2002-2006 Gargoyle Software Inc. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 * 3. The end-user documentation included with the redistribution, if any, must
 *    include the following acknowledgment:
 *
 *       "This product includes software developed by Gargoyle Software Inc.
 *        (http://www.GargoyleSoftware.com/)."
 *
 *    Alternately, this acknowledgment may appear in the software itself, if
 *    and wherever such third-party acknowledgments normally appear.
 * 4. The name "Gargoyle Software" must not be used to endorse or promote
 *    products derived from this software without prior written permission.
 *    For written permission, please contact info@GargoyleSoftware.com.
 * 5. Products derived from this software may not be called "HtmlUnit", nor may
 *    "HtmlUnit" appear in their name, without prior written permission of
 *    Gargoyle Software Inc.
 *
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED WARRANTIES,
 * INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 * FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL GARGOYLE
 * SOFTWARE INC. OR ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT,
 * INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA,
 * OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE,
 * EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package com.gargoylesoftware.htmlunit.html;

import java.util.Iterator;

import org.xml.sax.helpers.AttributesImpl;

import com.gargoylesoftware.htmlunit.WebTestCase;

/**
 * Tests for {@link DefaultElementFactory}.
 * @author <a href="mailto:marvin.java@gmail.com">Marcos Vinicius B. de Souza</a>
 * @author Marc Guillemot
 * @version $Revision$ 31/01/2006
 * @since 1.2
 */
public class DefaultElementFactoryTest extends WebTestCase {
    /**
     * Create an instance.
     * @param name The name of the test.
     */
    public DefaultElementFactoryTest( final String name ) {
        super( name );
    }

    /**
     * Test that the attribute order is the same as the provided one.
     * @throws Exception if the test fails
     */
    public void testAttributeOrder() throws Exception {
        // Construct the test page.
        final String html = "<html><head><title>test page</title></head>"
                + "<body><div>test message</div></body></html>";

        // Load the test page.
        final HtmlPage htmlPage = loadPage(html);

        // Creates the attributes of the 'anchor'.
        final AttributesImpl atts = new AttributesImpl();
        atts.addAttribute( null, "href", "href", null, "http://www.google.com" );
        atts.addAttribute( null, "tabindex", "tabindex", null, "2" );
        atts.addAttribute( null, "accesskey", "accessKey", null, "F" );

        // Access the factory.
        final DefaultElementFactory defaultElementFactory = new DefaultElementFactory( HtmlAnchor.class );

        // Create a anchor element
        final HtmlAnchor anchor = (HtmlAnchor) defaultElementFactory.createElement( htmlPage, "a", atts );

        verifyAttributes(anchor);
    }

    /**
     * @param anchor the anchor which attributes should be checked
     */
    private void verifyAttributes(final HtmlAnchor anchor) {
        // Get the attributes iterator
        final Iterator attributeEntriesIterator = anchor.getAttributeEntriesIterator();

        // Verify if the attributes are in ascending order of name.
        HtmlAttr htmlAttr = (HtmlAttr) attributeEntriesIterator.next();
        assertEquals( "href", htmlAttr.getNodeName() );
        assertEquals( "http://www.google.com", htmlAttr.getValue() );

        htmlAttr = (HtmlAttr) attributeEntriesIterator.next();
        assertEquals( "tabindex", htmlAttr.getNodeName() );
        assertEquals( "2", htmlAttr.getValue() );

        htmlAttr = (HtmlAttr) attributeEntriesIterator.next();
        assertEquals( "accesskey", htmlAttr.getNodeName() );
        assertEquals( "F", htmlAttr.getValue() );
    }

    /**
     * Test the order of attributes
     * @throws Exception if the test fails
     */
    public void testAttributeOrderLive() throws Exception {
        final String html = "<html><body>"
            + "<a href='http://www.google.com' tabindex='2' accesskey='F'>foo</a>"
            + "</body></html>";
        final HtmlPage page = loadPage(html);
        final HtmlAnchor anchor = page.getFirstAnchorByText("foo");

        verifyAttributes(anchor);
    }
}
