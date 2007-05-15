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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import com.gargoylesoftware.htmlunit.WebTestCase;

/**
 * Unit tests for {@link HtmlElement}.
 *
 * @version $Revision$
 * @author <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 * @author Denis N. Antonioli
 * @author Daniel Gredler
 */
public class HtmlElementTest extends WebTestCase {

    /**
     * Creates an instance.
     * @param name The name of the test.
     */
    public HtmlElementTest( final String name ) {
        super( name );
    }

    /**
     * Verifies that cloned node attributes have the same initial values, but changes can be made
     * to the clone without affecting the original node, and that the id attribute is treated the
     * same as all the other attributes. See bug 1707726.
     * @throws Exception If an error occurs.
     */
    public void testClonedNodeAttributes() throws Exception {
        final String html = "<html><body id='a' title='b'><script>\n" +
                "var x = document.body.cloneNode(true);\n" +
                "alert(document.body==x);\n" +
                "alert(document.getElementById('a')==document.body);\n" +
                "alert(document.body.id);\n" +
                "alert(x.id);\n" +
                "alert(document.body.title);\n" +
                "alert(x.title);\n" +
                "x.title='c';\n" +
                "alert(document.body.title);\n" +
                "alert(x.title);\n" +
                "</script></body></html>\n";
        final List collectedAlerts = new ArrayList();
        loadPage(html, collectedAlerts);
        final List expectedAlerts = Arrays.asList(new String[] {"false", "true", "a", "a", "b", "b", "b", "c"});
        assertEquals(expectedAlerts, collectedAlerts);
    }

    /**
     * @throws Exception if the test fails
     */
    public void testGetEnclosingForm() throws Exception {
        final String htmlContent = ""
            + "<html><head><title>foo</title></head><body>"
            + "<form id='form1'>"
            + "<table><tr><td><input type='text' id='foo'/></td></tr></table>"
            + "</form></body></html>";
        final HtmlPage page = loadPage(htmlContent);
        final HtmlForm form = (HtmlForm) page.getHtmlElementById("form1");

        final HtmlInput input = (HtmlInput) form.getHtmlElementById("foo");
        assertSame(form, input.getEnclosingForm());
    }

    /**
     * @throws Exception if the test fails
     */
    public void testGetEnclosing() throws Exception {
        final String htmlContent = ""
            + "<html><head><title>foo</title></head><body>"
            + "<form id='form1'>"
            + "<table id='table1'>"
            + "<tr id='tr1'><td id='td1'>foo</td></tr>"
            + "<tr id='tr2'><td id='td2'>foo</td></tr>"
            + "</table>"
            + "</form></body></html>";
        final HtmlPage page = loadPage(htmlContent);

        final HtmlElement td1 = page.getHtmlElementById("td1");
        assertEquals("tr1", td1.getEnclosingElement("tr").getId());
        assertEquals("tr1", td1.getEnclosingElement("TR").getId());
        assertEquals("table1", td1.getEnclosingElement("table").getId());
        assertEquals("form1", td1.getEnclosingElement("form").getId());

        final HtmlElement td2 = page.getHtmlElementById("td2");
        assertEquals("tr2", td2.getEnclosingElement("tr").getId());
        assertEquals("tr2", td2.getEnclosingElement("TR").getId());
        assertEquals("table1", td2.getEnclosingElement("table").getId());
        assertEquals("form1", td2.getEnclosingElement("form").getId());
    }

    /**
     * @throws Exception if the test fails
     */
    public void testAsText_WithComments() throws Exception {
        final String htmlContent
            = "<html><head><title>foo</title></head><body>"
            + "<p id='p1'>foo<!--bar--></p>"
            + "</body></html>";
        final HtmlPage page = loadPage(htmlContent);
        final HtmlElement element = page.getHtmlElementById("p1");
        assertEquals("foo", element.asText());
    }


    /**
     */
    public void testConstants() {
        assertEquals( "", HtmlElement.ATTRIBUTE_NOT_DEFINED );
        assertEquals( "", HtmlElement.ATTRIBUTE_VALUE_EMPTY );
        assertTrue( "Not the same object",
            HtmlElement.ATTRIBUTE_NOT_DEFINED != HtmlElement.ATTRIBUTE_VALUE_EMPTY );
    }

    /**
     */
    public void testEmtpyMapEntryWrappingIterator() {
        final Map attributsMap = new HashMap();
        final HtmlElement.MapEntryWrappingIterator it = 
            new HtmlElement.MapEntryWrappingIterator(attributsMap.entrySet().iterator(), HtmlAttrTest.HTML_ELEMENT);
        assertFalse(it.hasNext());
        try {
            it.next();
            fail("Travel empty iterator");
        }
        catch (final NoSuchElementException nsee) {
            // nothing, it's normal
        }
    }

    /**
     */
    public void testMapEntryWrappingIterator() {
        final Map attributsMap = new HashMap();
        attributsMap.put("key", "value");
        final HtmlElement.MapEntryWrappingIterator it = 
            new HtmlElement.MapEntryWrappingIterator(attributsMap.entrySet().iterator(), HtmlAttrTest.HTML_ELEMENT);

        assertTrue(it.hasNext());
        final Map.Entry me = (Map.Entry) it.next();
        assertEquals("key", me.getKey());
        assertEquals("value", me.getValue());
        assertInstanceOf(me, HtmlAttr.class);

        assertFalse(it.hasNext());
        try {
            it.next();
            fail("Travel empty iterator");
        }
        catch (final NoSuchElementException nsee) {
            // nothing, it's normal
        }
    }

    /**
     */
    public void testMapEntryWrappingIteratorDelegatesRemove() {
        final boolean removed[] = {false};

        final Iterator mockIterator = new Iterator() {
            public boolean hasNext() {
                return false;
            }

            public Object next() {
                return null;
            }

            public void remove() {
                removed[0] = true;
            }
        };

        final HtmlElement.MapEntryWrappingIterator it = 
            new HtmlElement.MapEntryWrappingIterator(mockIterator, HtmlAttrTest.HTML_ELEMENT);

        assertFalse(removed[0]);
        it.remove();
        assertTrue(removed[0]);

        // just for clover coverage
        assertFalse(mockIterator.hasNext());
        assertNull(mockIterator.next());
    }
}
