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
 * Set of tests for ill formed HTML code.
 * @author Marc Guillemot
 * @author Sudhan Moghe
 * @author Ahmed Ashour
 */
@RunWith(BrowserRunner.class)
public class MalformedHtml2Test extends SimpleWebTestCase {

    /**
     * Regression test for Bug #1018.
     * @throws Exception if an error occurs
     */
    @Test
    public void tableTextOutsideTD() throws Exception {
        final String html = "<html><body>\n"
            + "<table border='1'>\n"
            + "<tr><td>1</td>\n"
            + "<td>2</td>\n"
            + "some text\n"
            + "</tr>\n"
            + "</table>\n"
            + "</body></html>";
        final HtmlPage page = loadPageWithAlerts(html);
        final String expectedText = "some text" + System.lineSeparator()
            + "1\t2";
        assertEquals(expectedText, page.asText());
    }

}
