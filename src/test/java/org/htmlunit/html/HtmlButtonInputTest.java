/*
 * Copyright (c) 2002-2025 Gargoyle Software Inc.
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

import java.util.ArrayList;
import java.util.List;

import org.htmlunit.SimpleWebTestCase;
import org.htmlunit.junit.annotation.Alerts;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link HtmlButtonInput}.
 *
 * @author <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 * @author Ronald Brill
 */
public class HtmlButtonInputTest extends SimpleWebTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("foo")
    public void click_onClick() throws Exception {
        final String htmlContent = DOCTYPE_HTML
            + "<html>\n"
            + "<head></head>\n"
            + "<body>\n"
            + "<form id='form1' onSubmit='alert(\"bar\")'>\n"
            + "  <input type='button' name='button' id='button' onClick='alert(\"foo\")'>Push me</button>\n"
            + "</form>\n"
            + "</body></html>";

        final List<String> collectedAlerts = new ArrayList<>();
        final HtmlPage page = loadPage(htmlContent, collectedAlerts);
        final HtmlButtonInput button = page.getHtmlElementById("button");

        final HtmlPage secondPage = button.click();

        assertEquals(getExpectedAlerts(), collectedAlerts);

        assertSame(page, secondPage);
    }
}
