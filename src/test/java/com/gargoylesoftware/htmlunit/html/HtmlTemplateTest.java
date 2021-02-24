/*
 * Copyright (c) 2002-2021 Gargoyle Software Inc.
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
package com.gargoylesoftware.htmlunit.html;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.SimpleWebTestCase;

/**
 * Tests for {@link HtmlTemplate}.
 *
 * @author Ronny Shapiro
 * @author Ronald Brill
 */
@RunWith(BrowserRunner.class)
public class HtmlTemplateTest extends SimpleWebTestCase {

    @Test
    public void asXmlWithChildren() throws Exception {
        final String html = "<html>\n"
                + "<head>\n"
                + "</head>\n"
                + "<body>\n"
                + "<template id='template'>\n"
                + "<div></div>\n"
                + "</template>\n"
                + "</body>\n"
                + "</html>";

        final HtmlPage htmlPage = loadPage(html);
        assertEquals(htmlPage.getBody().asXml(), "<body>\r\n"
                + "  <template id=\"template\">\r\n"
                + "    <div>\r\n"
                + "    </div>\r\n"
                + "  </template>\r\n"
                + "</body>\r\n");
    }

    @Test
    public void asXmlWithoutChildren() throws Exception {
        final String html = "<html>\n"
                + "<head>\n"
                + "</head>\n"
                + "<body>\n"
                + "<template id='template'></template>\n"
                + "</body>\n"
                + "</html>";

        final HtmlPage htmlPage = loadPage(html);
        assertEquals(htmlPage.getBody().asXml(), "<body>\r\n"
                + "  <template id=\"template\">\r\n"
                + "  </template>\r\n"
                + "</body>\r\n");
    }
}
