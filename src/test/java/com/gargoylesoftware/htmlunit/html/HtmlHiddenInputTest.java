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

import org.junit.Test;

import com.gargoylesoftware.htmlunit.WebTestCase;

/**
 * Tests for {@link HtmlHiddenInput}.
 *
 * @version $Revision$
 * @author Marc Guillemot
 */
public class HtmlHiddenInputTest extends WebTestCase {

    /**
     * Verifies that a asText() returns "checked" or "unchecked" according to the state of the checkbox.
     * @throws Exception if the test fails
     */
    @Test
    public void testAsText() throws Exception {
        final String htmlContent
            = "<html><head><title>foo</title></head><body>\n"
            + "<form id='form1'>\n"
            + "    <input type='hidden' name='foo' id='foo' value='bla'>\n"
            + "</form></body></html>";

        final HtmlPage page = loadPage(htmlContent);

        final HtmlInput input = page.getHtmlElementById("foo");
        assertEquals("", input.asText());
    }
}
