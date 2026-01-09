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
package org.htmlunit.javascript;

import org.htmlunit.SimpleWebTestCase;
import org.htmlunit.html.HtmlPage;
import org.htmlunit.junit.annotation.Alerts;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link HtmlUnitScriptable}.
 *
 * @author Mike Bowler
 * @author Barnaby Court
 * @author David K. Taylor
 * @author Ben Curren
 * @author Marc Guillemot
 * @author Chris Erskine
 * @author Ahmed Ashour
 * @author Sudhan Moghe
 * @author Mike Dirolf
 * @author Frank Danek
 */
public class HtmlUnitScriptableTest extends SimpleWebTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("past focus")
    public void callInheritedFunction() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><title>foo</title><script>\n"
            + "function doTest() {\n"
            + "  document.form1.textfield1.focus();\n"
            + "  alert('past focus');\n"
            + "}\n"
            + "</script></head><body onload='doTest()'>\n"
            + "<p>hello world</p>\n"
            + "<form name='form1'>\n"
            + "  <input type='text' name='textfield1' id='textfield1' value='foo'/>\n"
            + "</form>\n"
            + "</body></html>";

        final HtmlPage page = loadPageWithAlerts(html);
        assertEquals("foo", page.getTitleText());
        assertSame("focus not changed to textfield1",
                     page.getFormByName("form1").getInputByName("textfield1"),
                     page.getFocusedElement());
    }
}
