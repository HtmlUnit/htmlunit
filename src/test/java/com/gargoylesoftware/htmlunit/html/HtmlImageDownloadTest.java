/*
 * Copyright (c) 2002-2008 Gargoyle Software Inc. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 * 3. The end-user documentation included with the redistribution, if any, must
 *    include the following acknowledgment:
 *
 *       "This product includes software developed by Gargoyle Software Inc.
 *        (http://www.GargoyleSoftware.com/)."
 *
 *    Alternately, this acknowledgment may appear in the software itself, if
 *    and wherever such third-party acknowledgments normally appear.
 * 4. The name "Gargoyle Software" must not be used to endorse or promote
 *    products derived from this software without prior written permission.
 *    For written permission, please contact info@GargoyleSoftware.com.
 * 5. Products derived from this software may not be called "HtmlUnit", nor may
 *    "HtmlUnit" appear in their name, without prior written permission of
 *    Gargoyle Software Inc.
 *
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED WARRANTIES,
 * INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 * FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL GARGOYLE
 * SOFTWARE INC. OR ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT,
 * INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA,
 * OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE,
 * EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
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
     *
     * @throws Exception if the test fails
     */
    @Test
    public void testImageHeight() throws Exception {
        final HtmlImage htmlimage = getHtmlElementToTest("image1");
        Assert.assertEquals("Image height", 612, htmlimage.getHeight());
    }

    /**
     *
     * @throws Exception if the test fails
     */
    @Test
    public void testImageWidth() throws Exception {
        final HtmlImage htmlimage = getHtmlElementToTest("image1");
        Assert.assertEquals("Image width", 879, htmlimage.getWidth());
    }

    /**
     *
     * @throws Exception if the test fails
     */
    @Test
    public void testImageFileSize() throws Exception {
        final HtmlImage htmlimage = getHtmlElementToTest("image1");
        Assert.assertEquals("Image filesize", 140144, htmlimage.getWebResponse(true).getResponseBody().length);
    }

    /**
     *
     * @throws Exception if the test fails
     */
    @Test
    public void testGetImageReader() throws Exception {
        final HtmlImage htmlimage = getHtmlElementToTest("image1");
        Assert.assertNotNull("ImageReader should not be null", htmlimage.getImageReader());
    }

     /**
     *
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
     *
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
     *
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
