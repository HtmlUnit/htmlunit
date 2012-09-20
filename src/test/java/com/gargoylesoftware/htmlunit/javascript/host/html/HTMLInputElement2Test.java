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
package com.gargoylesoftware.htmlunit.javascript.host.html;

import static com.gargoylesoftware.htmlunit.BrowserRunner.Browser.FF;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;
import com.gargoylesoftware.htmlunit.BrowserRunner.Browsers;
import com.gargoylesoftware.htmlunit.SimpleWebTestCase;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;

/**
 * Tests for {@link HTMLInputElement} and buttons.
 *
 * @version $Revision$
 * @author <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 * @author Marc Guillemot
 * @author Chris Erskine
 * @author Ahmed Ashour
 */
@RunWith(BrowserRunner.class)
public class HTMLInputElement2Test extends SimpleWebTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Browsers(FF)
    @Alerts("hello")
    public void selectionRange() throws Exception {
        final String html
            = "<html><head><title>foo</title><script>\n"
            + "function test() {\n"
            + "    var input = document.getElementById('myInput');\n"
            + "    input.setSelectionRange(2, 7);\n"
            + "    alert('hello');"
            + "}\n"
            + "</script></head><body onload='test()'>\n"
            + "<input id='myInput' value='some test'>\n"
            + "</body></html>";

        final HtmlPage page = loadPageWithAlerts(html);
        final HtmlTextInput input = page.getHtmlElementById("myInput");
        assertEquals("me te", input.getSelectedText());
    }

}
