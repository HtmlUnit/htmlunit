/*
 * Copyright (c) 2002-2007 Gargoyle Software Inc. All rights reserved.
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

import java.io.File;
import java.net.URI;

import com.gargoylesoftware.htmlunit.KeyDataPair;
import com.gargoylesoftware.htmlunit.MockWebConnection;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebTestCase;

/**
 * Tests for {@link HtmlFileInput}.
 *
 * @version $Revision$
 * @author Marc Guillemot
 * @author Ahmed Ashour
 */
public class HtmlFileInputTest extends WebTestCase {
    /**
     *  Create an instance
     * @param name The name of the test
     */
    public HtmlFileInputTest(final String name) {
        super(name);
    }

    /**
     * @throws Exception If the test fails.
     */
    public void testFileInput() throws Exception {
        String path = getClass().getResource("../testfiles/" + "tiny-png.img").toExternalForm();
        testFileInput(path);
        final File file = new File(new URI(path));
        testFileInput(file.getCanonicalPath());

        if (path.startsWith("file:")) {
            path = path.substring("file:".length());
        }
        while (path.startsWith("/")) {
            path = path.substring(1);
        }
        if (System.getProperty("os.name").toLowerCase().indexOf("windows") != -1) {
            testFileInput(path.replace('/', '\\'));
        }
        testFileInput("file:/" + path);
        testFileInput("file://" + path);
        testFileInput("file:///" + path);
    }

    private void testFileInput(final String fileURL) throws Exception {
        final String firstContent
            = "<html><head></head><body>\n"
            + "<form enctype='multipart/form-data' action='" + URL_SECOND + "' method='POST'>"
            + "  <input type='file' name='image' />\n"
            + "</form>\n"
            + "</body>\n"
            + "</html>";
        final String secondContent = "<html><head><title>second</title></head></html>";
        final WebClient client = new WebClient();

        final MockWebConnection webConnection = new MockWebConnection(client);
        webConnection.setResponse(URL_FIRST, firstContent);
        webConnection.setResponse(URL_SECOND, secondContent);

        client.setWebConnection(webConnection);
    
        final HtmlPage firstPage = (HtmlPage) client.getPage(URL_FIRST);
        final HtmlForm f = (HtmlForm) firstPage.getForms().get(0);
        final HtmlFileInput fileInput = (HtmlFileInput) f.getInputByName("image");
        fileInput.setValueAttribute(fileURL);
        f.submit((SubmittableElement) null);
        final KeyDataPair pair = (KeyDataPair) webConnection.getLastParameters().get(0);
        assertNotNull(pair.getFile());
        assertTrue(pair.getFile().length() != 0);
    }

    /**
     * Test content provided for a not filled file input
     * @throws Exception if the test fails
     */
    public void testEmptyField() throws Exception {
        final String firstContent
            = "<html><head></head><body>\n"
            + "<form enctype='multipart/form-data' action='" + URL_SECOND + "' method='POST'>"
            + "  <input type='file' name='image' />\n"
            + "</form>\n"
            + "</body>\n"
            + "</html>";
        final String secondContent = "<html><head><title>second</title></head></html>";
        final WebClient client = new WebClient();

        final MockWebConnection webConnection = new MockWebConnection(client);
        webConnection.setResponse(URL_FIRST, firstContent);
        webConnection.setResponse(URL_SECOND, secondContent);

        client.setWebConnection(webConnection);
    
        final HtmlPage firstPage = (HtmlPage) client.getPage(URL_FIRST);
        final HtmlForm f = (HtmlForm) firstPage.getForms().get(0);
        f.submit((SubmittableElement) null);
        final KeyDataPair pair = (KeyDataPair) webConnection.getLastParameters().get(0);
        assertEquals("image", pair.getName());
        assertNull(pair.getFile());
    }
}
