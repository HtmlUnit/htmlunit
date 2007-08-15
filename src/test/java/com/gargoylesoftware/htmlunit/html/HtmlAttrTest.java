/*
 * Copyright (c) 2002-2007 Gargoyle Software Inc. All rights reserved.
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
import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.gargoylesoftware.htmlunit.WebTestCase;

/**
 * Tests for {@link HtmlAttr}.
 *
 * @version $Revision$
 * @author Denis N. Antonioli
 * @author Ahmed Ashour
 */
public class HtmlAttrTest extends WebTestCase {
    /**
     * Test object.
     */
    private HtmlAttr htmlAttr_;
    /**
     * Single test key value.
     */
    private static final String ENTRY_KEY = "key";
    /**
     * Single test attribute value.
     */
    private static final String ENTRY_VALUE = "value";
    /**
     * Single test error message.
     */
    private static final String ENTRY_CANT_REMOVE = "Not implemented here";

    /**
     * A single Map.Entry for all the tests.
     */
    private static final Map.Entry MAP_ENTRY;

    /**
     * A single dummy HtmlElement. Necessary, because HtmlAttr's constructor calls the method getPage().
     */
    static final HtmlElement HTML_ELEMENT;

    static {
        MAP_ENTRY = new Map.Entry() {
            public Object getKey() {
                return ENTRY_KEY;
            }

            public Object getValue() {
                return ENTRY_VALUE;
            }

            public Object setValue(final Object o) {
                throw new NoSuchMethodError(ENTRY_CANT_REMOVE);
            }
        };
        HTML_ELEMENT = new HtmlElement(null, "dummy", null, Collections.EMPTY_MAP) {
            public HtmlPage getPage() {
                return null;
            }
        };
    }

    /**
     * Create an instance
     *
     * @param name The name of the test
     */
    public HtmlAttrTest(final String name) {
        super(name);
    }

    /**
     * {@inheritDoc}
     */
    protected void setUp() throws Exception {
        super.setUp();
        htmlAttr_ = new HtmlAttr(HTML_ELEMENT, MAP_ENTRY);
    }

    /**
     */
    public void testGetNodeName() {
        assertEquals(ENTRY_KEY, htmlAttr_.getNodeName());
    }

    /**
     */
    public void testGetNodeType() {
        assertEquals(org.w3c.dom.Node.ATTRIBUTE_NODE, htmlAttr_.getNodeType());
    }

    /**
     */
    public void testGetNodeValue() {
        assertEquals(ENTRY_VALUE, htmlAttr_.getNodeValue());
    }

    /**
     */
    public void testGetKey() {
        assertEquals(ENTRY_KEY, htmlAttr_.getKey());
    }

    /**
     */
    public void testGetValue() {
        assertEquals(ENTRY_VALUE, htmlAttr_.getHtmlValue());
    }

    /**
     */
    public void testSetValue() {
        try {
            htmlAttr_.setHtmlValue("foo");
            fail("Method not implemented!");
        }
        catch (final NoSuchMethodError nsme) {
            assertEquals(ENTRY_CANT_REMOVE, nsme.getMessage());
        }
    }

    /**
     */
    public void testGetParent() {
        assertSame(HTML_ELEMENT, htmlAttr_.getParentDomNode());
    }

    /**
     * Test nodeType of {@link Attribute}.
     *
     * @throws Exception if the test fails
     */
    public void testNodeType() throws Exception {
        final String content = "<html><head><title>foo</title><script>"
            + "  function test() {\n"
            + "    var attr = document.createAttribute('myAttrib');\n"
            + "    alert(attr.nodeType);\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";
        final String[] expectedAlerts = {"2"};
        final List collectedAlerts = new ArrayList();
        loadPage(content, collectedAlerts);
        assertEquals(expectedAlerts, collectedAlerts);
    }
}
