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

import static org.junit.Assert.fail;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.net.URL;

import org.apache.commons.io.IOUtils;
import org.htmlunit.WebClient;
import org.htmlunit.WebResponse;
import org.htmlunit.WebServerTestCase;
import org.htmlunit.http.HttpHeader;
import org.htmlunit.junit.BrowserRunner;
import org.htmlunit.platform.image.ImageData;
import org.htmlunit.platform.image.ImageIOImageData;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Tests for {@link HtmlImage}.
 *
 * @author Knut Johannes Dahle
 * @author Ahmed Ashour
 * @author Marc Guillemot
 * @author Lukas Botsch
 */
@RunWith(BrowserRunner.class)
public class HtmlImageDownloadTest extends WebServerTestCase {
    private static final String base_file_path_ = "src/test/resources/org/htmlunit/html";

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
        final HtmlImage htmlImage = getHtmlElementToTest("image1");
        assertEquals("Image height", 612, htmlImage.getHeight());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void imageWidth() throws Exception {
        final HtmlImage htmlImage = getHtmlElementToTest("image1");
        assertEquals("Image width", 879, htmlImage.getWidth());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void imageFileSize() throws Exception {
        final HtmlImage htmlImage = getHtmlElementToTest("image1");
        try (InputStream in = htmlImage.getWebResponse(true).getContentAsStream()) {
            assertEquals("Image filesize", 140144,
                    IOUtils.toByteArray(in).length);
        }
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void getImageData() throws Exception {
        final HtmlImage htmlImage = getHtmlElementToTest("image1");
        assertNotNull("ImageData should not be null", htmlImage.getImageData());
        assertNotNull("ImageReader should not be null", ((ImageIOImageData) htmlImage.getImageData()).getImageReader());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void getImageDataNoneSupportedImage() throws Exception {
        final HtmlImage htmlImage = getHtmlElementToTest("image1");
        final String url = "/HtmlImageDownloadTest.html";
        htmlImage.setAttribute("src", url);
        try {
            htmlImage.getImageData();
            fail("it was not an image!");
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
        final HtmlImage htmlImage = getHtmlElementToTest("image1");
        final URL url = htmlImage.getPage().getUrl();
        // per default images are not downloaded
        assertNull(htmlImage.getWebResponse(false));
        final WebResponse resp = htmlImage.getWebResponse(true);
        assertNotNull(resp);
        assertEquals(url.toExternalForm(), resp.getWebRequest().getAdditionalHeaders().get(HttpHeader.REFERER));
    }

    /**
     * The image should be redownloaded when the src attribute changes.
     * @throws Exception if the test fails
     */
    @Test
    public void redownloadOnSrcAttributeChanged() throws Exception {
        final HtmlImage htmlImage = getHtmlElementToTest("image1");

        final ImageData imageData = htmlImage.getImageData();

        htmlImage.setAttribute("src", htmlImage.getSrcAttribute() + "#changed");

        assertFalse("Src attribute changed but ImageData was not reloaded",
                imageData.equals(htmlImage.getImageData()));
    }

    /**
     * Common code for the tests to load the test page and fetch the HtmlImage object.
     * @param id value of image id attribute
     * @return the found HtmlImage
     * @throws Exception if an error occurs
     */
    private HtmlImage getHtmlElementToTest(final String id) throws Exception {
        final String url = URL_FIRST + "HtmlImageDownloadTest.html";
        final WebClient client = getWebClient();
        final HtmlPage page = client.getPage(url);
        return (HtmlImage) page.getElementById(id);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @After
    public void tearDown() throws Exception {
        Thread.sleep(100);
        super.tearDown();
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void serialize() throws Exception {
        final HtmlImage htmlImage = getHtmlElementToTest("image1");
        htmlImage.getImageData();

        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            try (ObjectOutputStream out = new ObjectOutputStream(baos)) {
                out.writeObject(htmlImage);
            }
        }
    }

}
