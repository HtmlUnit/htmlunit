/*
 * Copyright (c) 2002-2013 Gargoyle Software Inc.
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

import static org.junit.Assert.assertSame;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.SimpleWebTestCase;

/**
 * Tests for {@link DomAttr}.
 *
 * @version $Revision$
 * @author Denis N. Antonioli
 * @author Ahmed Ashour
 * @author David K. Taylor
 */
@RunWith(BrowserRunner.class)
public class HtmlAttrTest extends SimpleWebTestCase {

    /** Test object. */
    private final DomAttr htmlAttr_ = new DomAttr(null, null, ENTRY_KEY, ENTRY_VALUE, false);

    /** Single test key value. */
    private static final String ENTRY_KEY = "key";

    /** Single test attribute value. */
    private static final String ENTRY_VALUE = "value";

    /** A single dummy HtmlElement. Necessary, because HtmlAttr's constructor calls the method getPage(). */
    static final HtmlElement HTML_ELEMENT;

    static {
        final Map<String, DomAttr> emptyMap = Collections.emptyMap();
        HTML_ELEMENT = new HtmlElement(null, "dummy", null, emptyMap) {
            @Override
            public HtmlPage getPage() {
                return null;
            }
        };
    }

    /**
     * Constructor.
     */
    public HtmlAttrTest() {
        htmlAttr_.setParentNode(HTML_ELEMENT);
    }

    /**
     * Tests {@link DomAttr#getName()}.
     */
    @Test
    public void testGetName() {
        assertEquals(ENTRY_KEY, htmlAttr_.getName());
    }

    /**
     * Tests {@link DomAttr#getNodeName()}.
     */
    @Test
    public void testGetNodeName() {
        assertEquals(ENTRY_KEY, htmlAttr_.getNodeName());
    }

    /**
     * Tests {@link DomAttr#getNodeType()}.
     */
    @Test
    public void testGetNodeType() {
        assertEquals(org.w3c.dom.Node.ATTRIBUTE_NODE, htmlAttr_.getNodeType());
    }

    /**
     * Tests {@link DomAttr#getNodeValue()}.
     */
    @Test
    public void testGetNodeValue() {
        assertEquals(ENTRY_VALUE, htmlAttr_.getNodeValue());
    }

    /**
     * Tests {@link DomAttr#getValue()}.
     */
    @Test
    public void testGetValue() {
        assertEquals(ENTRY_VALUE, htmlAttr_.getValue());
    }

    /**
     * Tests {@link DomAttr#setValue(String)}.
     */
    @Test
    public void testSetValue() {
        htmlAttr_.setValue("foo");
        assertEquals("foo", htmlAttr_.getValue());
    }

    /**
     * Tests {@link DomAttr#getParentNode()}.
     */
    @Test
    public void testGetParent() {
        assertSame(HTML_ELEMENT, htmlAttr_.getParentNode());
    }

    /**
     * Test nodeType of {@link DomAttr}.
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
