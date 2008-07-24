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
import com.gargoylesoftware.htmlunit.html.HtmlButtonInput;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

/**
 * Unit tests for {@link HTMLBodyElement}.
 *
 * @version $Revision$
 * @author Daniel Gredler
 * @author Ahmed Ashour
 */
public class HTMLBodyElementTest extends WebTestCase {

    /**
     * Tests the default body padding and margins.
     * @throws Exception if an error occurs
     */
    @Test
    public void testDefaultPaddingAndMargins() throws Exception {
        testDefaultPaddingAndMargins(BrowserVersion.FIREFOX_2, ",0px,0px,0px,0px", ",,,,", ",8px,8px,8px,8px", ",,,,");
        testDefaultPaddingAndMargins(BrowserVersion.INTERNET_EXPLORER_7_0, "0px,0px,0px,0px,0px", ",,,,", "15px 10px,10px,10px,15px,15px", ",,,,");
        testDefaultPaddingAndMargins(BrowserVersion.INTERNET_EXPLORER_6_0, "0px,0px,0px,0px,0px", ",,,,", "15px 10px,10px,10px,15px,15px", ",,,,");
    }

    private void testDefaultPaddingAndMargins(final BrowserVersion version, final String... expected) throws Exception {
        final String html =
            "<html>\n"
            + "  <head>\n"
            + "    <script>\n"
            + "      function test() {\n"
            + "        var b = document.getElementById('body');\n"
            + "        var s = b.currentStyle ? b.currentStyle : getComputedStyle(b, null);\n"
            + "        alert(s.padding + ',' + s.paddingLeft + ',' + s.paddingRight + ',' + s.paddingTop + ',' + s.paddingBottom);\n"
            + "        alert(b.style.padding + ',' + b.style.paddingLeft + ',' + b.style.paddingRight + ',' + b.style.paddingTop + ',' + b.style.paddingBottom);\n"
            + "        alert(s.margin + ',' + s.marginLeft + ',' + s.marginRight + ',' + s.marginTop + ',' + s.marginBottom);\n"
            + "        alert(b.style.margin + ',' + b.style.marginLeft + ',' + b.style.marginRight + ',' + b.style.marginTop + ',' + b.style.marginBottom);\n"
            + "      }\n"
            + "    </script>\n"
            + "  </head>\n"
            + "  <body id='body' onload='test()'>blah</body>\n"
            + "</html>";
        final List<String> collectedAlerts = new ArrayList<String>();
        loadPage(version, html, collectedAlerts);
        assertEquals(expected, collectedAlerts);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void attachEvent() throws Exception {
        final String html =
            "<html>\n"
            + "  <head>\n"
            + "    <script>\n"
            + "      function handler() {\n"
            + "        alert(event);\n"
            + "      }\n"
            + "      function test() {\n"
            + "        document.body.attachEvent('onclick', handler);\n"
            + "      }\n"
            + "    </script>\n"
            + "  </head>\n"
            + "  <body onload='test()'>\n"
            + "    <input type='button' id='myInput' value='Test me'>\n"
            + "  </body>\n"
            + "</html>";
        final String[] expectedAlerts = {"[object]"};
        final List<String> collectedAlerts = new ArrayList<String>();
        final HtmlPage page = loadPage(BrowserVersion.INTERNET_EXPLORER_7_0, html, collectedAlerts);
		((HtmlButtonInput) page.getHtmlElementById("myInput")).click();
        assertEquals(expectedAlerts, collectedAlerts);
    }
}
