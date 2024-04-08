/*
 * Copyright (c) 2002-2024 Gargoyle Software Inc.
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

import org.htmlunit.MockWebConnection;
import org.htmlunit.Page;
import org.htmlunit.SimpleWebTestCase;
import org.htmlunit.http.HttpMethod;
import org.htmlunit.junit.BrowserRunner;
import org.htmlunit.util.NameValuePair;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Tests for {@link HtmlIsIndex}.
 *
 * @author <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 * @author Marc Guillemot
 * @author Ahmed Ashour
 */
@RunWith(BrowserRunner.class)
public class HtmlIsIndexTest extends SimpleWebTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void formSubmission() throws Exception {
        final String html
            = "<html><head><title>foo</title></head><body>\n"
                + "<form id='form1' method='post'>\n"
                + "  <isindex prompt='enterSomeText'></isindex>\n"
                + "  <input type='submit' id='clickMe'>\n"
                + "</form></body></html>";
        final HtmlPage page = loadPage(html);
        final MockWebConnection webConnection = getMockConnection(page);

        final HtmlForm form = page.getHtmlElementById("form1");

        final HtmlElement element = form.getElementsByAttribute(
                "isindex", "prompt", "enterSomeText").get(0);
        if (element instanceof HtmlIsIndex) {
            final HtmlIsIndex isInput = (HtmlIsIndex) element;
            isInput.setValue("Flintstone");
            final Page secondPage = page.getHtmlElementById("clickMe").click();

            final List<NameValuePair> expectedParameters = new ArrayList<>();
            expectedParameters.add(new NameValuePair("enterSomeText", "Flintstone"));

            assertEquals("url", URL_FIRST, secondPage.getUrl());
            assertSame("method", HttpMethod.POST, webConnection.getLastMethod());
            assertEquals("parameters", expectedParameters, webConnection.getLastParameters());
        }
    }
}
