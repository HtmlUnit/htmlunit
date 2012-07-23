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

import static org.junit.Assert.assertSame;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;
import com.gargoylesoftware.htmlunit.WebTestCase;

/**
 * Tests for {@link HtmlButtonInput}.
 *
 * @version $Revision$
 * @author <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 */
@RunWith(BrowserRunner.class)
public class HtmlButtonInputTest extends WebTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("foo")
    public void click_onClick() throws Exception {
        final String htmlContent
            = "<html><head><title>foo</title></head><body>\n"
            + "<form id='form1' onSubmit='alert(\"bar\")'>\n"
            + "    <input type='button' name='button' id='button' "
            + "onClick='alert(\"foo\")'>Push me</button>\n"
            + "</form></body></html>";

        final List<String> collectedAlerts = new ArrayList<String>();
        final HtmlPage page = loadPage(htmlContent, collectedAlerts);
        final HtmlButtonInput button = page.getHtmlElementById("button");

        final HtmlPage secondPage = button.click();

        final String[] expectedAlerts = {"foo"};
        assertEquals(expectedAlerts, collectedAlerts);

        assertSame(page, secondPage);
    }
}
