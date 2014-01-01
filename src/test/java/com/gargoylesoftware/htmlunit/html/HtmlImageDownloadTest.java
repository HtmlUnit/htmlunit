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

import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageReader;

import org.apache.commons.io.IOUtils;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebResponse;
import com.gargoylesoftware.htmlunit.WebServerTestCase;

/**
 * Tests for {@link HtmlImage}.
 *
 * @version $Revision$
 * @author Knut Johannes Dahle
 * @author Ahmed Ashour
 * @author Marc Guillemot
 * @author Lukas Botsch
 */
@RunWith(BrowserRunner.class)
public class HtmlImageDownloadTest extends WebServerTestCase {
    private static final String base_file_path_ = "src/test/resources/com/gargoylesoftware/htmlunit/html";

    /**
     * Constructor.
     * @throws Exception if an exception occurs
     */
    public HtmlImageDownloadTest() throws Exception {
        startWebServer(base_file_path_);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void imageHeight() throws Exception {
        final HtmlImage htmlimage = getHtmlElementToTest("image1");
        Assert.assertEquals("Image height", 612, htmlimage.getHeight());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void imageWidth() throws Exception {
        final HtmlImage htmlimage = getHtmlElementToTest("image1");
        Assert.assertEquals("Image width", 879, htmlimage.getWidth());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void imageFileSize() throws Exception {
        final HtmlImage htmlimage = getHtmlElementToTest("image1");
        Assert.assertEquals("Image filesize", 140144,
                IOUtils.toByteArray(htmlimage.getWebResponse(true).getContentAsStream()).length);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void getImageReader() throws Exception {
        final HtmlImage htmlimage = getHtmlElementToTest("image1");
        Assert.assertNotNull("ImageReader should not be null", htmlimage.getImageReader());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void getImageReaderNoneSupportedImage() throws Exception {
        final HtmlImage htmlimage = getHtmlElementToTest("image1");
        final String url = "/HtmlImageDownloadTest.html";
        htmlimage.setAttribute("src", url);
        try {
            htmlimage.getImageReader();
            Assert.fail("it was not an image!");
        }
        catch (final IOException ioe) {
            // Correct behavior
        }
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void getWebResponse() throws Exception {
        final HtmlImage htmlimage = getHtmlElementToTest("image1");
        final URL url = htmlimage.getPage().getUrl();
        Assert.assertNull(htmlimage.getWebResponse(false));
        final WebResponse resp = htmlimage.getWebResponse(true);
        Assert.assertNotNull(resp);
        assertEquals(url.toExternalForm(), resp.getWebRequest().getAdditionalHeaders().get("Referer"));
    }

    /**
     * The image should be redownloaded when the src attribute changes.
     * @throws Exception if the test fails
     */
    @Test
    public void redownloadOnSrcAttributeChanged() throws Exception {
        final HtmlImage htmlimage = getHtmlElementToTest("image1");
        final ImageReader imagereader = htmlimage.getImageReader();
        htmlimage.setAttribute("src", htmlimage.getAttribute("src") + "#changed");
        Assert.assertFalse("Src attribute changed but ImageReader was not reloaded",
                imagereader.equals(htmlimage.getImageReader()));
    }

    /**
     * Common code for the tests to load the test page and fetch the HtmlImage object.
     * @param id value of image id attribute
     * @return the found HtmlImage
     * @throws Exception if an error occurs
     */
    private HtmlImage getHtmlElementToTest(final String id) throws Exception {
        final String url = "http://localhost:" + PORT + "/HtmlImageDownloadTest.html";
        final WebClient client = getWebClient();
        final HtmlPage page = client.getPage(url);
        return (HtmlImage) page.getElementById(id);
    }

    /**
     * {@inheritDoc}
     */
    @After
    public void tearDown() throws Exception {
        Thread.sleep(100);
        super.tearDown();
    }
}
