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

import static org.junit.Assert.assertSame;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.MockWebConnection;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebTestCase;
import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;
import com.gargoylesoftware.htmlunit.util.NameValuePair;

/**
 * Tests for {@link HtmlIsIndex}.
 *
 * @version $Revision$
 * @author <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 * @author Marc Guillemot
 * @author Ahmed Ashour
 */
@RunWith(BrowserRunner.class)
public class HtmlIsIndexTest extends WebTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void testFormSubmission() throws Exception {
        final String html
            = "<html><head><title>foo</title></head><body>\n"
            + "<form id='form1' method='post'>\n"
            + "<isindex prompt='enterSomeText'></isindex>\n"
            + "</form></body></html>";
        final HtmlPage page = loadPage(html);
        final MockWebConnection webConnection = getMockConnection(page);

        final HtmlForm form = page.getHtmlElementById("form1");

        final HtmlIsIndex isInput = form.<HtmlIsIndex>getElementsByAttribute(
                "isindex", "prompt", "enterSomeText").get(0);
        isInput.setValue("Flintstone");
        final Page secondPage = form.submit((SubmittableElement) null);

        final List<NameValuePair> expectedParameters = new ArrayList<NameValuePair>();
        expectedParameters.add(new NameValuePair("enterSomeText", "Flintstone"));

        assertEquals("url", getDefaultUrl(), secondPage.getWebResponse().getWebRequest().getUrl());
        assertSame("method", HttpMethod.POST, webConnection.getLastMethod());
        Assert.assertEquals("parameters", expectedParameters, webConnection.getLastParameters());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(FF = "[object HTMLIsIndexElement]", IE = "[object]")
    public void simpleScriptable() throws Exception {
        final String html = "<html><head>\n"
            + "<script>\n"
            + "  function test() {\n"
            + "    alert(document.getElementById('myId'));\n"
            + "  }\n"
            + "</script>\n"
            + "</head><body onload='test()'>\n"
            + "<form id='form1' method='post'>\n"
            + "<isindex id='myId' prompt='enterSomeText'></isindex>\n"
            + "</form></body></html>";

        final HtmlPage page = loadPageWithAlerts(html);
        assertTrue(HtmlIsIndex.class.isInstance(page.getHtmlElementById("myId")));
    }
}
