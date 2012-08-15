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
package com.gargoylesoftware.htmlunit;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.gargoylesoftware.htmlunit.html.HtmlPage;

/**
 * Tests for {@link WebResponse}.
 *
 * @version $Revision$
 * @author Marc Guillemot
 * @author Ahmed Ashour
 */
@RunWith(BrowserRunner.class)
public class WebResponse2Test extends WebTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void charsetInMetaTag() throws Exception {
        final String html
            = "<html>\n"
            + "<head><meta content='text/html; charset=utf-8' http-equiv='Content-Type'/></head>\n"
            + "<body>foo</body>\n"
            + "</html>";
        final HtmlPage page = loadPage(html);
        assertEquals("utf-8", page.getWebResponse().getContentCharsetOrNull());
        assertEquals(html, page.getWebResponse().getContentAsString());
    }

}
