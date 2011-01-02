/*
 * Copyright (c) 2002-2011 Gargoyle Software Inc.
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
import org.junit.runner.RunWith;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.WebDriverTestCase;
import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;

/**
 * Tests for {@link HtmlInsertedText}.
 *
 * @version $Revision$
 * @author Marc Guillemot
 */
@RunWith(BrowserRunner.class)
public class HtmlInsertedTextTest extends WebDriverTestCase {
    /**
     * Regression test for 3033189: &lt;ins&gt; elements are inline elements.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("abcdef")
    public void simple() throws Exception {
        final String html = "<html><body>\n"
            + "<a href='foo' id='it'>ab<ins>cd</ins>ef</a>\n"
            + "<script>\n"
            + "var e = document.getElementById('it');\n"
            + "alert(e.textContent || e.innerText);\n"
            + "</script>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }
}
