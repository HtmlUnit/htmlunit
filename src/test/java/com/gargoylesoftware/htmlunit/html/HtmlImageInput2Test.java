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

import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.MockWebConnection;
import com.gargoylesoftware.htmlunit.SimpleWebTestCase;
import com.gargoylesoftware.htmlunit.util.NameValuePair;

/**
 * Tests for {@link HtmlImageInput}.
 *
 * @author <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 * @author Marc Guillemot
 * @author Ahmed Ashour
 */
@RunWith(BrowserRunner.class)
public class HtmlImageInput2Test extends SimpleWebTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void click_WithPosition() throws Exception {
        final String html
            = "<html><head><title>foo</title></head><body>\n"
            + "<form id='form1' method='post'>\n"
            + "<input type='image' name='aButton' value='foo'/>\n"
            + "<input type='image' name='button' value='foo'/>\n"
            + "<input type='image' name='anotherButton' value='foo'/>\n"
            + "</form></body></html>";
        final HtmlPage page = loadPageWithAlerts(html);
        final MockWebConnection webConnection = getMockConnection(page);

        final HtmlForm form = page.getHtmlElementById("form1");

        final HtmlImageInput imageInput = form.getInputByName("button");
        final HtmlPage secondPage = imageInput.click(100, 200);
        assertNotNull(secondPage);

        final List<NameValuePair> expectedPairs = new ArrayList<>();
        expectedPairs.add(new NameValuePair("button.x", "100"));
        expectedPairs.add(new NameValuePair("button.y", "200"));

        if (getBrowserVersion().isChrome() || getBrowserVersion().isEdge()) {
            expectedPairs.add(new NameValuePair("button", "foo"));
        }

        assertEquals(expectedPairs, webConnection.getLastParameters());
    }

    /**
     * If an image button without name is clicked, it should send only "x" and "y" parameters.
     * Regression test for bug #217.
     * @throws Exception if the test fails
     */
    @Test
    public void noNameClick_WithPosition() throws Exception {
        final String html
            = "<html><head><title>foo</title></head><body>\n"
            + "<form id='form1' method='post'>\n"
            + "<input type='image' value='foo'/>\n"
            + "</form></body></html>";
        final HtmlPage page = loadPageWithAlerts(html);
        final MockWebConnection webConnection = getMockConnection(page);

        final HtmlForm form = page.getHtmlElementById("form1");

        final HtmlImageInput imageInput = form.getInputByValue("foo");
        final HtmlPage secondPage = imageInput.click(100, 200);
        assertNotNull(secondPage);

        final List<NameValuePair> expectedPairs = Arrays.asList(new NameValuePair[]{
            new NameValuePair("x", "100"),
            new NameValuePair("y", "200")
        });

        assertEquals(expectedPairs, webConnection.getLastParameters());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void saveAs() throws Exception {
        try (InputStream is = getClass().getClassLoader().
                getResourceAsStream("testfiles/tiny-jpg.img")) {
            final byte[] directBytes = IOUtils.toByteArray(is);
            final URL urlImage = new URL(URL_FIRST, "img.jpg");
            final List<NameValuePair> emptyList = Collections.emptyList();
            getMockWebConnection().setResponse(urlImage, directBytes, 200, "ok", "image/jpg", emptyList);
        }

        final String html = "<html><head>\n"
            + "</head>\n"
            + "<body>\n"
            + "  <input type='image' src='img.jpg' >\n"
            + "</body></html>";

        final HtmlPage page = loadPage(html);

        final HtmlImageInput input = page.querySelector("input");
        final File tempFile = File.createTempFile("img", ".tmp");
        input.saveAs(tempFile);
        FileUtils.deleteQuietly(tempFile);
    }

}
