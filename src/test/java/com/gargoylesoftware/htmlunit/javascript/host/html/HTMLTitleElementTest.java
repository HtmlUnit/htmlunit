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
package com.gargoylesoftware.htmlunit.javascript.host.html;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;
import com.gargoylesoftware.htmlunit.SimpleWebTestCase;

/**
 * Unit tests for {@link HTMLTitleElement}.
 * @version $Revision$
 * @author Sudhan Moghe
 */
@RunWith(BrowserRunner.class)
public class HTMLTitleElementTest extends SimpleWebTestCase {
    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"Page Title", "New Title" })
    public void text() throws Exception {
        final String html =
            "<html>\n"
            + "  <head>\n"
            + "    <title>Page Title</title>\n"
            + "    <script>\n"
            + "      function test() {\n"
            + "         var title = document.getElementsByTagName('title')[0];\n"
            + "         alert(title.text);\n"
            + "         title.text='New Title';\n"
            + "         alert(title.text);\n"
            + "      }\n"
            + "    </script>\n"
            + "  </head>\n"
            + "  <body onload='test()'>\n"
            + "  </body>\n"
            + "</html>";
        loadPageWithAlerts(html);
    }
}
