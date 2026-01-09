/*
 * Copyright (c) 2002-2026 Gargoyle Software Inc.
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

import org.htmlunit.SimpleWebTestCase;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link HtmlTemplate}.
 *
 * @author Ronny Shapiro
 * @author Ronald Brill
 */
public class HtmlTemplate2Test extends SimpleWebTestCase {

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void asXmlWithChildren() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html>\n"
                + "<head>\n"
                + "</head>\n"
                + "<body>\n"
                + "<template id='template'>\n"
                + "<div></div>\n"
                + "</template>\n"
                + "</body>\n"
                + "</html>";

        final HtmlPage htmlPage = loadPage(html);
        assertEquals("<body>\r\n"
                + "  <template id=\"template\">\r\n"
                + "    <div></div>\r\n"
                + "  </template>\r\n"
                + "</body>", htmlPage.getBody().asXml());
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void asXmlWithoutChildren() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html>\n"
                + "<head>\n"
                + "</head>\n"
                + "<body>\n"
                + "<template id='template'></template>\n"
                + "</body>\n"
                + "</html>";

        final HtmlPage htmlPage = loadPage(html);
        assertEquals("<body>\r\n"
                + "  <template id=\"template\"></template>\r\n"
                + "</body>", htmlPage.getBody().asXml());
    }
}
