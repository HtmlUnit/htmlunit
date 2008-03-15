/*
 * Copyright (c) 2002-2008 Gargoyle Software Inc. All rights reserved.
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

import static org.junit.Assert.assertSame;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import com.gargoylesoftware.htmlunit.WebTestCase;

/**
 * Tests for {@link HtmlAttr}.
 *
 * @version $Revision$
 * @author Denis N. Antonioli
 * @author Ahmed Ashour
 * @author David K. Taylor
 */
public class HtmlAttrTest extends WebTestCase {

    /** Test object. */
    private HtmlAttr htmlAttr_;

    /** Single test key value. */
    private static final String ENTRY_KEY = "key";

    /** Single test attribute value. */
    private static final String ENTRY_VALUE = "value";

    /** A single dummy HtmlElement. Necessary, because HtmlAttr's constructor calls the method getPage(). */
    static final HtmlElement HTML_ELEMENT;

    static {
        final Map<String, HtmlAttr> emptyMap = Collections.emptyMap();
        HTML_ELEMENT = new HtmlElement(null, "dummy", null, emptyMap) {
            private static final long serialVersionUID = -3099722791571459332L;

            @Override
            public HtmlPage getPage() {
                return null;
            }
        };
    }

    /**
     * Performs pre-test initialization.
     */
    @Before
    public void setUp() {
        htmlAttr_ = new HtmlAttr(null, null, ENTRY_KEY, ENTRY_VALUE);
        htmlAttr_.setParentNode(HTML_ELEMENT);
    }

    /**
     * Tests {@link HtmlAttr#getName()}.
     */
    @Test
    public void testGetName() {
        assertEquals(ENTRY_KEY, htmlAttr_.getName());
    }

    /**
     * Tests {@link HtmlAttr#getNodeName()}.
     */
    @Test
    public void testGetNodeName() {
        assertEquals(ENTRY_KEY, htmlAttr_.getNodeName());
    }

    /**
     * Tests {@link HtmlAttr#getNodeType()}.
     */
    @Test
    public void testGetNodeType() {
        assertEquals(org.w3c.dom.Node.ATTRIBUTE_NODE, htmlAttr_.getNodeType());
    }

    /**
     * Tests {@link HtmlAttr#getNodeValue()}.
     */
    @Test
    public void testGetNodeValue() {
        assertEquals(ENTRY_VALUE, htmlAttr_.getNodeValue());
    }

    /**
     * Tests {@link HtmlAttr#getHtmlValue()}.
     */
    @Test
    public void testGetValue() {
        assertEquals(ENTRY_VALUE, htmlAttr_.getHtmlValue());
    }

    /**
     * Tests {@link HtmlAttr#setHtmlValue(String)}.
     */
    @Test
    public void testSetValue() {
        htmlAttr_.setHtmlValue("foo");
        assertEquals("foo", htmlAttr_.getHtmlValue());
    }

    /**
     * Tests {@link HtmlAttr#getParentDomNode()}.
     */
    @Test
    public void testGetParent() {
        assertSame(HTML_ELEMENT, htmlAttr_.getParentDomNode());
    }

    /**
     * Test nodeType of {@link HtmlAttr}.
     *
     * @throws Exception if the test fails
     */
    @Test
    public void testNodeType() throws Exception {
        final String content = "<html><head><title>foo</title><script>\n"
            + "  function test() {\n"
            + "    var attr = document.createAttribute('myAttrib');\n"
            + "    alert(attr.nodeType);\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";
        final String[] expectedAlerts = {"2"};
        final List<String> collectedAlerts = new ArrayList<String>();
        loadPage(content, collectedAlerts);
        assertEquals(expectedAlerts, collectedAlerts);
    }
}
