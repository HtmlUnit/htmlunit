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
package com.gargoylesoftware.htmlunit.html;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.WebTestCase;

/**
 * Set of tests for ill formed HTML code.
 * @version $Revision$
 * @author Marc Guillemot
 * @author Sudhan Moghe
 * @author Ahmed Ashour
 */
@RunWith(BrowserRunner.class)
public class MalformedHtml2Test extends WebTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void incompleteEntities() throws Exception {
        final String html = "<html><head>\n"
            + "<title>Test document</title>\n"
            + "</head><body>\n"
            + "<a href='foo?a=1&copy=2&prod=3' id='myLink'>my link</a>\n"
            + "</body></html>";

        final HtmlPage page = loadPage(getBrowserVersion(), html, null);
        final HtmlPage page2 = page.getAnchors().get(0).click();

        final String query;
        if (getBrowserVersion().isIE()) {
            query = "a=1\u00A9=2&prod=3";
        }
        else {
            query = "a=1%A9=2&prod=3";
        }
        assertEquals(query, page2.getWebResponse().getWebRequest().getUrl().getQuery());
    }

    /**
     * Regression test for bug 2940936.
     * @throws Exception if an error occurs
     */
    @Test
    public void tableTextOutsideTD() throws Exception {
        final String html = "<html><body>"
            + "<table border='1'>\n"
            + "<tr><td>1</td>\n"
            + "<td>2</td>\n"
            + "some text\n"
            + "</tr>\n"
            + "</table>\n"
            + "</body></html>";
        final HtmlPage page = loadPageWithAlerts(html);
        final String expectedText = "some text" + LINE_SEPARATOR
            + "1\t2";
        assertEquals(expectedText, page.asText());
    }

}
