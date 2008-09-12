/*
 * Copyright (c) 2002-2008 Gargoyle Software Inc.
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
package com.gargoylesoftware.htmlunit.javascript.host;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.WebTestCase;

/**
 * Unit tests for {@link HTMLSpanElement}.
 *
 * @version $Revision$
 * @author Daniel Gredler
 */
public class HTMLSpanElementTest extends WebTestCase {

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void doScroll() throws Exception {
        final String html =
            "<html>\n"
            + "  <head>\n"
            + "    <script>\n"
            + "      function test() {\n"
            + "        var span = document.getElementById('s');\n"
            + "        if(span.doScroll) {\n"
            + "          alert('yes');\n"
            + "          span.doScroll();\n"
            + "          span.doScroll('down');\n"
            + "        } else {\n"
            + "          alert('no');\n"
            + "        }\n"
            + "      }\n"
            + "    </script>\n"
            + "  </head>\n"
            + "  <body onload='test()'><span id='s'>abc</span></body>\n"
            + "</html>";

        String[] expectedAlerts = new String[] {"no"};
        List<String> collectedAlerts = new ArrayList<String>();
        loadPage(BrowserVersion.FIREFOX_3, html, collectedAlerts);
        assertEquals(expectedAlerts, collectedAlerts);

        expectedAlerts = new String[] {"yes"};
        collectedAlerts = new ArrayList<String>();
        loadPage(BrowserVersion.INTERNET_EXPLORER_7_0, html, collectedAlerts);
        assertEquals(expectedAlerts, collectedAlerts);
    }

}
