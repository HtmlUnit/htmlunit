/*
 * Copyright (c) 2002-2025 Gargoyle Software Inc.
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
package org.htmlunit.html;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.htmlunit.SimpleWebTestCase;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link DomAttr}.
 *
 * @author Denis N. Antonioli
 * @author Ahmed Ashour
 * @author David K. Taylor
 * @author Frank Danek
 * @author Ronald Brill
 */
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
        HTML_ELEMENT = new HtmlElement("dummy", null, emptyMap) {
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
    public void getName() {
        assertEquals(ENTRY_KEY, htmlAttr_.getName());
    }

    /**
     * Tests {@link DomAttr#getNodeName()}.
     */
    @Test
    public void getNodeName() {
        assertEquals(ENTRY_KEY, htmlAttr_.getNodeName());
    }

    /**
     * Tests {@link DomAttr#getNodeType()}.
     */
    @Test
    public void getNodeType() {
        assertEquals(org.w3c.dom.Node.ATTRIBUTE_NODE, htmlAttr_.getNodeType());
    }

    /**
     * Tests {@link DomAttr#getNodeValue()}.
     */
    @Test
    public void getNodeValue() {
        assertEquals(ENTRY_VALUE, htmlAttr_.getNodeValue());
    }

    /**
     * Tests {@link DomAttr#getValue()}.
     */
    @Test
    public void getValue() {
        assertEquals(ENTRY_VALUE, htmlAttr_.getValue());
    }

    /**
     * Tests {@link DomAttr#setValue(String)}.
     */
    @Test
    public void setValue() {
        htmlAttr_.setValue("foo");
        assertEquals("foo", htmlAttr_.getValue());
    }

    /**
     * Tests {@link DomAttr#getParentNode()}.
     */
    @Test
    public void getParent() {
        assertSame(HTML_ELEMENT, htmlAttr_.getParentNode());
    }

    /**
     * Test nodeType of {@link DomAttr}.
     *
     * @throws Exception if the test fails
     */
    @Test
    public void nodeType() throws Exception {
        final String content = DOCTYPE_HTML
            + "<html><head><title>foo</title><script>\n"
            + "  function test() {\n"
            + "    var attr = document.createAttribute('myAttrib');\n"
            + "    alert(attr.nodeType);\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";
        final String[] expectedAlerts = {"2"};
        final List<String> collectedAlerts = new ArrayList<>();
        loadPage(content, collectedAlerts);
        assertEquals(expectedAlerts, collectedAlerts);
    }
}
