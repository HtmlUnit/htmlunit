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
package com.gargoylesoftware.htmlunit.html.impl;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.w3c.dom.Node;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.SimpleWebTestCase;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

/**
 * Tests for SimpleRange.
 *
 * @version $Revision$
 * @author Ronald Brill
 */
@RunWith(BrowserRunner.class)
public class SimpleRangeTest extends SimpleWebTestCase {

    /**
     * @throws Exception if test fails
     */
    @Test
    public void toStringOneNode() throws Exception {
        final String content = "<html><head><title>Test page</title></head>\n"
            + "<body>\n"
            + "  <input type='text' id='myInput' value='abcd'>"
            + "</body>\n"
            + "</html>";

        final HtmlPage page = loadPage(content);
        final Node node = page.getElementById("myInput");

        // select all
        SimpleRange range = new SimpleRange(node, 0, node, 4);
        assertEquals("abcd", range.toString());

        // select part
        range = new SimpleRange(node, 1, node, 3);
        assertEquals("bc", range.toString());

        // wrong start offset
        range = new SimpleRange(node, 7, node, 3);
        assertEquals("", range.toString());

        // wrong end offset
        range = new SimpleRange(node, 0, node, 11);
        assertEquals("abcd", range.toString());
    }
}
