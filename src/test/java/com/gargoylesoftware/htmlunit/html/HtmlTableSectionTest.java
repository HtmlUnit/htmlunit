/*
 * Copyright (c) 2002-2014 Gargoyle Software Inc.
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
import com.gargoylesoftware.htmlunit.SimpleWebTestCase;

/**
 * Tests for {@link HtmlTableBody}, {@link HtmlTableHeader}, and {@link HtmlTableFooter}.
 *
 * @version $Revision$
 * @author <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 * @author Ahmed Ashour
 */
@RunWith(BrowserRunner.class)
public class HtmlTableSectionTest extends SimpleWebTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void asText() throws Exception {
        final String html = "<html>\n"
            + "<body>\n"
            + "  <table>\n"
            + "    <tfoot><td>Five</td></tfoot>\n"
            + "    <tbody><td>Two</td></tbody>\n"
            + "    <thead><td>One</td></thead>\n"
            + "    <thead><td>Three</td></thead>\n"
            + "    <tfoot><td>Four</td></tfoot>\n"
            + "  </table>\n"
            + "</body></html>";

        final HtmlPage page = loadPageWithAlerts(html);
        assertEquals("One" + LINE_SEPARATOR + "Two" + LINE_SEPARATOR + "Three" + LINE_SEPARATOR
                + "Four" + LINE_SEPARATOR + "Five", page.asText());
    }
}
