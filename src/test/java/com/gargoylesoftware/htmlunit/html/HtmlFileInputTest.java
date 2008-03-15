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

import static org.junit.Assert.assertNotNull;

import java.io.File;
import java.io.IOException;
import java.io.Writer;
import java.net.URI;
import java.net.URL;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadBase;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.multipart.FilePart;
import org.apache.commons.httpclient.methods.multipart.MultipartRequestEntity;
import org.apache.commons.httpclient.methods.multipart.Part;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import org.mortbay.jetty.Server;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.HttpWebConnectionTest;
import com.gargoylesoftware.htmlunit.KeyDataPair;
import com.gargoylesoftware.htmlunit.MockWebConnection;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebTestCase;
import com.gargoylesoftware.htmlunit.util.ServletContentWrapper;

/**
 * Tests for {@link HtmlFileInput}.
 *
 * @version $Revision$
 * @author Marc Guillemot
 * @author Ahmed Ashour
 */
public class HtmlFileInputTest extends WebTestCase {

    private Server server_;

    /**
     * @throws Exception If the test fails.
     */
    @Test
    public void testFileInput() throws Exception {
        String path = getClass().getClassLoader().getResource("testfiles/" + "tiny-png.img").toExternalForm();
        testFileInput(path);
        final File file = new File(new URI(path));
        testFileInput(file.getCanonicalPath());

        if (path.startsWith("file:")) {
            path = path.substring("file:".length());
        }
        while (path.startsWith("/")) {
            path = path.substring(1);
        }
        if (System.getProperty("os.name").toLowerCase().contains("windows")) {
            testFileInput(URLDecoder.decode(path.replace('/', '\\'), "UTF-8"));
        }
        testFileInput("file:/" + path);
        testFileInput("file://" + path);
        testFileInput("file:///" + path);
    }

    private void testFileInput(final String fileURL) throws Exception {
        final String firstContent = "<html><head></head><body>\n"
            + "<form enctype='multipart/form-data' action='" + URL_SECOND + "' method='POST'>\n"
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
    @Test
    public void testEmptyField() throws Exception {
        final String firstContent = "<html><head></head><body>\n"
            + "<form enctype='multipart/form-data' action='" + URL_SECOND + "' method='POST'>\n"
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

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void testContentType() throws Exception {
        final String firstContent = "<html><head></head><body>\n"
            + "<form enctype='multipart/form-data' action='" + URL_SECOND + "' method='POST'>\n"
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

        final URL fileURL = getClass().getClassLoader().getResource("testfiles/empty.png");

        fileInput.setValueAttribute(fileURL.toExternalForm());
        f.submit((SubmittableElement) null);
        final KeyDataPair pair = (KeyDataPair) webConnection.getLastParameters().get(0);
        assertNotNull(pair.getFile());
        Assert.assertFalse("Content type: " + pair.getContentType(), "text/webtest".equals(pair.getContentType()));

        fileInput.setContentType("text/webtest");
        f.submit((SubmittableElement) null);
        final KeyDataPair pair2 = (KeyDataPair) webConnection.getLastParameters().get(0);
        assertNotNull(pair2.getFile());
        assertEquals("text/webtest", pair2.getContentType());
    }

    /**
     * Test HttpClient for uploading a file with non-ASCII name, if it works it means HttpClient has fixed its bug.
     *
     * Test for http://issues.apache.org/jira/browse/HTTPCLIENT-293,
     * which is related to http://sourceforge.net/tracker/index.php?func=detail&aid=1818569&group_id=47038&atid=448266
     *
     * @throws Exception If the test fails.
     */
    @Test
    public void testUploadFileWithNonASCIIName_HttpClient() throws Exception {
        if (notYetImplemented()) {
            return;
        }
        
        final String filename = "\u6A94\u6848\uD30C\uC77C\u30D5\u30A1\u30A4\u30EB\u0645\u0644\u0641.txt";
        final String path = getClass().getClassLoader().getResource(filename).toExternalForm();
        final File file = new File(new URI(path));
        assertTrue(file.exists());
        
        
        final Map<Class< ? extends Servlet>, String> servlets = new HashMap<Class< ? extends Servlet>, String>();
        servlets.put(Upload2Servlet.class, "/upload2");

        server_ = HttpWebConnectionTest.startWebServer("./", null, servlets);
        final PostMethod filePost = new PostMethod("http://localhost:" + HttpWebConnectionTest.PORT + "/upload2");
        
        final FilePart part = new FilePart("myInput", file);
        part.setCharSet("UTF-8");

        filePost.setRequestEntity(new MultipartRequestEntity(new Part[] {part}, filePost.getParams()));
        final HttpClient client = new HttpClient();
        client.executeMethod(filePost);

        final String response = filePost.getResponseBodyAsString();
        //this is the value with ASCII encoding
        assertFalse("3F 3F 3F 3F 3F 3F 3F 3F 3F 3F 3F 2E 74 78 74 <br>myInput".equals(response));
    }

    /**
     * Test uploading a file with non-ASCII name.
     *
     * Test for http://sourceforge.net/tracker/index.php?func=detail&aid=1818569&group_id=47038&atid=448266
     *
     * @throws Exception If the test fails.
     */
    @Test
    public void testUploadFileWithNonASCIIName() throws Exception {
        final Map<Class< ? extends Servlet>, String> servlets = new HashMap<Class< ? extends Servlet>, String>();
        servlets.put(Upload1Servlet.class, "/upload1");
        servlets.put(Upload2Servlet.class, "/upload2");
        server_ = HttpWebConnectionTest.startWebServer("./", null, servlets);

        testUploadFileWithNonASCIIName(BrowserVersion.FIREFOX_2);
        testUploadFileWithNonASCIIName(BrowserVersion.INTERNET_EXPLORER_7_0);
    }

    private void testUploadFileWithNonASCIIName(final BrowserVersion browserVersion) throws Exception {
        final String filename = "\u6A94\u6848\uD30C\uC77C\u30D5\u30A1\u30A4\u30EB\u0645\u0644\u0641.txt";
        final String path = getClass().getClassLoader().getResource(filename).toExternalForm();
        final File file = new File(new URI(path));
        assertTrue(file.exists());
        
        final WebClient client = new WebClient(browserVersion);
        final HtmlPage firstPage = (HtmlPage) client.getPage(
                new URL("http://localhost:" + HttpWebConnectionTest.PORT + "/upload1"));

        final HtmlForm form = (HtmlForm) firstPage.getForms().get(0);
        final HtmlFileInput fileInput = (HtmlFileInput) form.getInputByName("myInput");
        fileInput.setValueAttribute(path);
        
        final HtmlSubmitInput submitInput = (HtmlSubmitInput) form.getInputByValue("Upload");
        final HtmlPage secondPage = (HtmlPage) submitInput.click();

        final String response = secondPage.getWebResponse().getContentAsString();

        //this is the value with UTF-8 encoding
        final String expectedResponse = "6A94 6848 D30C C77C 30D5 30A1 30A4 30EB 645 644 641 2E 74 78 74 <br>myInput";
        
        assertTrue("Invalid Response: " + response, response.contains(expectedResponse));
        
        if (browserVersion.isIE()) {
            assertTrue(expectedResponse.length() < response.length());
        }
        else {
            assertEquals(expectedResponse.length(), response.length());
        }
    }

    /**
     * Servlet for '/upload1'
     */
    public static class Upload1Servlet extends ServletContentWrapper {

        private static final long serialVersionUID = 6693252829875297263L;

        /**
         * Creates an instance.
         */
        public Upload1Servlet() {
            super("<html><head><meta http-equiv='Content-Type' content='text/html; charset=utf-8'/></head>\n"
            + "<form action='upload2' method='post' enctype='multipart/form-data'>\n"
            + "Name: <input name='myInput' type='file'><br>\n"
            + "Name 2 (should stay empty): <input name='myInput2' type='file'><br>\n"
            + "<input type='submit' value='Upload' id='mySubmit'>\n"
            + "</form>\n");
        }
    }

    /**
     * Servlet for '/upload2'
     */
    public static class Upload2Servlet extends HttpServlet {

        private static final long serialVersionUID = -1350878755076138012L;

        /**
         * {@inheritDoc}
         */
        @Override
        @SuppressWarnings("unchecked")
        protected void doPost(final HttpServletRequest request, final HttpServletResponse response)
            throws ServletException, IOException {
            request.setCharacterEncoding("UTF-8");
            response.setContentType("text/html");
            final Writer writer = response.getWriter();
            if (ServletFileUpload.isMultipartContent(request)) {
                try {
                    final ServletFileUpload upload = new ServletFileUpload(new DiskFileItemFactory());
                    for (final FileItem item : (List<FileItem>) upload.parseRequest(request)) {
                        if ("myInput".equals(item.getFieldName())) {
                            final String path = item.getName();
                            for (int i = 0; i < path.length(); i++) {
                                writer.write(Integer.toHexString(path.charAt(i)).toUpperCase() + " ");
                            }
                            writer.write("<br>");
                            writer.write(item.getFieldName());
                        }
                    }
                }
                catch (final FileUploadBase.SizeLimitExceededException e) {
                    writer.write("SizeLimitExceeded");
                }
                catch (final Exception e) {
                    writer.write("error");
                }
            }
            writer.close();
        }
    }

    /**
     * Performs post-test deconstruction.
     * @throws Exception if an error occurs
     */
    @After
    public void tearDown() throws Exception {
        HttpWebConnectionTest.stopWebServer(server_);
        server_ = null;
    }
}
