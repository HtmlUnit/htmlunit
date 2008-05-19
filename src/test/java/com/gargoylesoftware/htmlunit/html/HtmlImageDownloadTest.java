/*
 * Copyright (c) 2002-2008 Gargoyle Software Inc.
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

import org.junit.Assert;
import org.junit.Test;
import org.junit.Before;
import org.junit.After;
import org.mortbay.jetty.Server;

import com.gargoylesoftware.htmlunit.WebTestCase;
import com.gargoylesoftware.htmlunit.HttpWebConnectionTest;

import java.net.URL;
import java.io.IOException;

/**
 * Tests for {@link HtmlImage}.
 *
 * @version $Revision$
 * @author Knut Johannes Dahle
 */

public class HtmlImageDownloadTest extends WebTestCase {
    private Server server_;
    private final String base_file_path_ = "src/test/resources/com/gargoylesoftware/htmlunit/html";
    private URL url_;

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void testImageHeight() throws Exception {
        final HtmlImage htmlimage = getHtmlElementToTest("image1");
        Assert.assertEquals("Image height", 612, htmlimage.getHeight());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void testImageWidth() throws Exception {
        final HtmlImage htmlimage = getHtmlElementToTest("image1");
        Assert.assertEquals("Image width", 879, htmlimage.getWidth());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void testImageFileSize() throws Exception {
        final HtmlImage htmlimage = getHtmlElementToTest("image1");
        Assert.assertEquals("Image filesize", 140144, htmlimage.getWebResponse(true).getResponseBody().length);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void testGetImageReader() throws Exception {
        final HtmlImage htmlimage = getHtmlElementToTest("image1");
        Assert.assertNotNull("ImageReader should not be null", htmlimage.getImageReader());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void testGetImageReaderNoneSupportedImage() throws Exception {
        final HtmlImage htmlimage = getHtmlElementToTest("image1");
        final String url = "/HtmlImageDownloadTest.html";
        htmlimage.setAttribute("src", url);
        try {
            htmlimage.getImageReader();
            Assert.fail("it was not an image!");
        }
        catch (final IOException ioe) {
            // Correct behaviour
        }
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void testGetWebResponse() throws Exception {
        final HtmlImage htmlimage = getHtmlElementToTest("image1");
        Assert.assertNull(htmlimage.getWebResponse(false));
        Assert.assertNotNull(htmlimage.getWebResponse(true));
    }

    /**
     * Performs pre-test initialization.
     * @throws Exception if an error occurs
     */
    @Before
    public void setup() throws Exception {
        server_ = HttpWebConnectionTest.startWebServer(base_file_path_);
        url_ = new URL("http", "localhost", HttpWebConnectionTest.PORT, "");
    }

    /**
     * Performs post-test deconstruction.
     * @throws Exception if an error occurs
     */
    @After
    public void teardown() throws Exception {
        HttpWebConnectionTest.stopWebServer(server_);
        server_ = null;
    }

    /**
     * Common code for the tests to load the testpage and fecth the HtmlImage object.
     * @param id value of img id attribute
     * @return the found HtmlImage
     * @throws Exception if an error occurs
     */
    private HtmlImage getHtmlElementToTest(final String id) throws Exception {
        final String url = url_.toString() + "/HtmlImageDownloadTest.html";
        final HtmlPage page = loadUrl(url);
        return (HtmlImage) page.getElementById(id);
    }
}
