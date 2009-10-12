/*
 * Copyright (c) 2002-2009 Gargoyle Software Inc.
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
package com.gargoylesoftware.htmlunit.libraries;

import static org.junit.Assert.assertNotNull;

import java.net.URL;

import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebDriverTestCase;
import com.gargoylesoftware.htmlunit.BrowserRunner.NotYetImplemented;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlSpan;

/**
 * <p>Tests for compatibility with <a href="http://tinymce.moxiecode.com/">TinyMCE</a>.</p>
 *
 * <p>TODO: API test is "not yet implemented"</p>
 * <p>TODO: more tests to add (not just API)</p>
 *
 * @version $Revision$
 * @author Daniel Gredler
 */
@RunWith(BrowserRunner.class)
public class TinyMceTest extends WebDriverTestCase {

    private WebClient client_;

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @NotYetImplemented
    public void api() throws Exception {
        test("api", 348, 0);
    }

    private void test(final String fileName, final int expectedTotal, final int expectedFailed) throws Exception {
        final URL url = getClass().getClassLoader().getResource("tinymce/3.2.7/tests/" + fileName + ".html");
        assertNotNull(url);

        client_ = new WebClient(getBrowserVersion());
        final HtmlPage page = (HtmlPage) client_.getPage(url);
        client_.waitForBackgroundJavaScript(5000L);

        final HtmlElement result = page.getElementById("testresult");
        final HtmlSpan totalSpan = result.getFirstByXPath("span[@class='all']");
        final int total = Integer.parseInt(totalSpan.asText());
        assertEquals(expectedTotal, total);

        final HtmlSpan failedSpan = result.getFirstByXPath("span[@class='bad']");
        final int failed = Integer.parseInt(failedSpan.asText());
        assertEquals(expectedFailed, failed);
    }

    /**
     * Performs post-test deconstruction.
     * @throws Exception if an error occurs
     */
    @After
    public void tearDown() throws Exception {
        if (client_ != null) {
            client_.closeAllWindows();
        }
    }

}
