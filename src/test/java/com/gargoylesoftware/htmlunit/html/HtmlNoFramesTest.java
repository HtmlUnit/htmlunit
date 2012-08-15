/*
 * Copyright (c) 2002-2012 Gargoyle Software Inc.
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

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.SimpleWebTestCase;

/**
 * Tests for elements inside {@link HtmlNoFrames}.
 *
 * @version $Revision$
 * @author Ahmed Ashour
 * @author Marc Guillemot
 */
@RunWith(BrowserRunner.class)
public class HtmlNoFramesTest extends SimpleWebTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void preserveInnerXML() throws Exception {
        final String html
            = "<html>\n"
            + "  <noframes>\n"
            + "    <b>Some bold text</b>\n"
            + "  </noframes>\n"
            + "</html>";

        final HtmlPage page = loadPage(html);
        assertTrue(page.asXml().contains("&lt;b&gt;"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void asText() throws Exception {
        final String html
            = "<html>\n"
            + "  <noframes id='it'>\n"
            + "    Some text\n"
            + "  </noframes>\n"
            + "</html>";

        final HtmlPage page = loadPage(html);
        assertEquals("", page.getElementById("it").asText());
        Assert.assertFalse(page.asText(), page.asText().contains("Some text"));
    }
}
