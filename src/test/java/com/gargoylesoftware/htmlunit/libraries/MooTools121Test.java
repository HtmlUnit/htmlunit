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
package com.gargoylesoftware.htmlunit.libraries;

import static org.junit.Assert.assertNotNull;

import java.io.File;
import java.net.URL;
import java.util.List;

import junit.framework.AssertionFailedError;

import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.SimpleWebTestCase;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

/**
 * Tests for compatibility with version 1.2.1 of the <a href="http://mootools.net/">MooTools JavaScript library</a>.
 *
 * @version $Revision$
 * @author Daniel Gredler
 */
@RunWith(BrowserRunner.class)
public class MooTools121Test extends SimpleWebTestCase {

    private WebClient client_;

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @SuppressWarnings("unchecked")
    public void mooTools() throws Exception {
        final String resource = "libraries/mootools/1.2.1/Specs/index.html";
        final URL url = getClass().getClassLoader().getResource(resource);
        assertNotNull(url);

        client_ = getWebClient();
        final HtmlPage page = client_.getPage(url);

        final HtmlElement progress = page.getHtmlElementById("progress");
        // usually this need 40s but sometimes our build machine is slower
        // this is not an performance test, we only like to ensure that all
        // functionality is running
        final int jobCount = client_.waitForBackgroundJavaScriptStartingBefore(60 * DEFAULT_WAIT_TIME);
        assertTrue("There are still " + jobCount + " jobs running", jobCount == 0);

        final String prevProgress = progress.asText();

        FileUtils.writeStringToFile(new File("/tmp/mootols.html"), page.asXml());
        final String xpath = "//ul[@class='specs']/li[@class!='success']";
        final List<HtmlElement> failures = (List<HtmlElement>) page.getByXPath(xpath);
        if (!failures.isEmpty()) {
            final StringBuilder sb = new StringBuilder();
            for (HtmlElement failure : failures) {
                sb.append(failure.asXml()).append("\n\n");
            }
            throw new AssertionFailedError(sb.toString());
        }

        assertEquals("364", page.getElementById("total_examples").asText());
        assertEquals("0", page.getElementById("total_failures").asText());
        assertEquals("0", page.getElementById("total_errors").asText());
        assertEquals("100", prevProgress);
    }

    /**
     * Performs post-test deconstruction.
     */
    @After
    public void tearDown() {
        client_.closeAllWindows();
    }

}
