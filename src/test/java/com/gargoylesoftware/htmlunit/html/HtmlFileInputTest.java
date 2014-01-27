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

import static com.gargoylesoftware.htmlunit.BrowserRunner.Browser.NONE;
import static org.junit.Assert.assertNotNull;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Writer;
import java.lang.reflect.Method;
import java.net.URI;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadBase;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.HttpClientBuilder;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.BrowserRunner.Browsers;
import com.gargoylesoftware.htmlunit.HttpWebConnection;
import com.gargoylesoftware.htmlunit.MockWebConnection;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.WebServerTestCase;
import com.gargoylesoftware.htmlunit.util.KeyDataPair;

/**
 * Tests for {@link HtmlFileInput}.
 *
 * @version $Revision$
 * @author Marc Guillemot
 * @author Ahmed Ashour
 * @author Ronald Brill
 */
@RunWith(BrowserRunner.class)
public class HtmlFileInputTest extends WebServerTestCase {

    /**
     * @throws Exception if the test fails
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
            + "  <input type='file' name='image'>\n"
            + "  <input type='submit' id='clickMe'>\n"
            + "</form>\n"
            + "</body>\n"
            + "</html>";
        final String secondContent = "<html><head><title>second</title></head></html>";
        final WebClient client = getWebClient();

        final MockWebConnection webConnection = new MockWebConnection();
        webConnection.setResponse(URL_FIRST, firstContent);
        webConnection.setResponse(URL_SECOND, secondContent);

        client.setWebConnection(webConnection);

        final HtmlPage firstPage = client.getPage(URL_FIRST);
        final HtmlForm f = firstPage.getForms().get(0);
        final HtmlFileInput fileInput = f.getInputByName("image");
        fileInput.setValueAttribute(fileURL);
        firstPage.getHtmlElementById("clickMe").click();
        final KeyDataPair pair = (KeyDataPair) webConnection.getLastParameters().get(0);
        assertNotNull(pair.getFile());
        assertTrue(pair.getFile().length() != 0);
    }

    /**
     * Tests setData method.
     * @throws Exception if the test fails
     */
    @Test
    public void setValueAttributeAndSetDataDummyFile() throws Exception {
        final String firstContent = "<html><head></head><body>\n"
            + "<form enctype='multipart/form-data' action='" + URL_SECOND + "' method='POST'>\n"
            + "  <input type='file' name='image'>\n"
            + "  <input type='submit' id='mySubmit'>\n"
            + "</form>\n"
            + "</body>\n"
            + "</html>";
        final String secondContent = "<html><head><title>second</title></head></html>";
        final WebClient client = getWebClient();

        final MockWebConnection webConnection = new MockWebConnection();
        webConnection.setResponse(URL_FIRST, firstContent);
        webConnection.setResponse(URL_SECOND, secondContent);

        client.setWebConnection(webConnection);

        final HtmlPage firstPage = client.getPage(URL_FIRST);
        final HtmlForm f = firstPage.getForms().get(0);
        final HtmlFileInput fileInput = f.getInputByName("image");
        fileInput.setValueAttribute("dummy.txt");
        fileInput.setContentType("text/csv");
        fileInput.setData("My file data".getBytes());
        firstPage.getHtmlElementById("mySubmit").click();
        final KeyDataPair pair = (KeyDataPair) webConnection.getLastParameters().get(0);

        assertNotNull(pair.getData());
        assertTrue(pair.getData().length > 10);

        final Method makeHttpMethod = HttpWebConnection.class.getDeclaredMethod("makeHttpMethod", WebRequest.class);
        makeHttpMethod.setAccessible(true);
        final HttpPost httpPost = (HttpPost) makeHttpMethod
            .invoke(new HttpWebConnection(client), webConnection.getLastWebRequest());
        final HttpEntity httpEntity = httpPost.getEntity();

        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        httpEntity.writeTo(out);
        out.close();

        Assert.assertTrue(
                out.toString().contains("Content-Disposition: form-data; name=\"image\"; filename=\"dummy.txt\""));
    }

    /**
     * Tests setData method.
     * @throws Exception if the test fails
     */
    @Test
    public void setValueAttributeAndSetDataRealFile() throws Exception {
        final String firstContent = "<html><head></head><body>\n"
            + "<form enctype='multipart/form-data' action='" + URL_SECOND + "' method='POST'>\n"
            + "  <input type='file' name='image'>\n"
            + "  <input type='submit' id='mySubmit'>\n"
            + "</form>\n"
            + "</body>\n"
            + "</html>";
        final String secondContent = "<html><head><title>second</title></head></html>";
        final WebClient client = getWebClient();

        final MockWebConnection webConnection = new MockWebConnection();
        webConnection.setResponse(URL_FIRST, firstContent);
        webConnection.setResponse(URL_SECOND, secondContent);

        client.setWebConnection(webConnection);

        final HtmlPage firstPage = client.getPage(URL_FIRST);
        final HtmlForm f = firstPage.getForms().get(0);
        final HtmlFileInput fileInput = f.getInputByName("image");
        final String path = getClass().getClassLoader().getResource("testfiles/" + "tiny-png.img").toExternalForm();
        fileInput.setValueAttribute(path);
        fileInput.setData("My file data".getBytes());
        firstPage.getHtmlElementById("mySubmit").click();
        final KeyDataPair pair = (KeyDataPair) webConnection.getLastParameters().get(0);

        assertNotNull(pair.getData());
        assertTrue(pair.getData().length > 10);

        final Method makeHttpMethod = HttpWebConnection.class.getDeclaredMethod("makeHttpMethod", WebRequest.class);
        makeHttpMethod.setAccessible(true);
        final HttpPost httpPost = (HttpPost) makeHttpMethod
            .invoke(new HttpWebConnection(client), webConnection.getLastWebRequest());
        final HttpEntity httpEntity = httpPost.getEntity();

        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        httpEntity.writeTo(out);
        out.close();

        if (getBrowserVersion().isIE()) {
            final Pattern pattern = Pattern
                .compile("Content-Disposition: form-data; name=\"image\";"
                        + " filename=\".*testfiles[\\\\/]tiny-png\\.img\"");
            final Matcher matcher = pattern.matcher(out.toString());
            assertTrue(matcher.find());
        }
        else if (getBrowserVersion().isFirefox()) {
            assertTrue(out.toString()
                    .contains("Content-Disposition: form-data; name=\"image\"; filename=\"tiny-png.img\""));
        }
    }

    /**
     * Tests setData method.
     * @throws Exception if the test fails
     */
    @Test
    public void setDataOnly() throws Exception {
        final String firstContent = "<html><head></head><body>\n"
            + "<form enctype='multipart/form-data' action='" + URL_SECOND + "' method='POST'>\n"
            + "  <input type='file' name='image'>\n"
            + "  <input type='submit' id='mySubmit'>\n"
            + "</form>\n"
            + "</body>\n"
            + "</html>";
        final String secondContent = "<html><head><title>second</title></head></html>";
        final WebClient client = getWebClient();

        final MockWebConnection webConnection = new MockWebConnection();
        webConnection.setResponse(URL_FIRST, firstContent);
        webConnection.setResponse(URL_SECOND, secondContent);

        client.setWebConnection(webConnection);

        final HtmlPage firstPage = client.getPage(URL_FIRST);
        final HtmlForm f = firstPage.getForms().get(0);
        final HtmlFileInput fileInput = f.getInputByName("image");
        fileInput.setData("My file data".getBytes());
        firstPage.getHtmlElementById("mySubmit").click();
        final KeyDataPair pair = (KeyDataPair) webConnection.getLastParameters().get(0);

        assertNull(pair.getData());

        final Method makeHttpMethod = HttpWebConnection.class.getDeclaredMethod("makeHttpMethod", WebRequest.class);
        makeHttpMethod.setAccessible(true);
        final HttpPost httpPost = (HttpPost) makeHttpMethod
            .invoke(new HttpWebConnection(client), webConnection.getLastWebRequest());
        final HttpEntity httpEntity = httpPost.getEntity();

        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        httpEntity.writeTo(out);
        out.close();

        assertTrue(out.toString()
                .contains("Content-Disposition: form-data; name=\"image\"; filename=\"\""));
    }

    /**
     * Verifies that content is provided for a not filled file input.
     * @throws Exception if the test fails
     */
    @Test
    public void testEmptyField() throws Exception {
        final String firstContent = "<html><head></head><body>\n"
            + "<form enctype='multipart/form-data' action='" + URL_SECOND + "' method='POST'>\n"
            + "  <input type='file' name='image' />\n"
            + "  <input type='submit' id='clickMe'>\n"
            + "</form>\n"
            + "</body>\n"
            + "</html>";
        final String secondContent = "<html><head><title>second</title></head></html>";
        final WebClient client = getWebClient();

        final MockWebConnection webConnection = new MockWebConnection();
        webConnection.setResponse(URL_FIRST, firstContent);
        webConnection.setResponse(URL_SECOND, secondContent);

        client.setWebConnection(webConnection);

        final HtmlPage firstPage = client.getPage(URL_FIRST);
        firstPage.getHtmlElementById("clickMe").click();
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
            + "  <input type='submit' name='mysubmit'/>\n"
            + "</form>\n"
            + "</body>\n"
            + "</html>";
        final String secondContent = "<html><head><title>second</title></head></html>";
        final WebClient client = getWebClient();

        final MockWebConnection webConnection = new MockWebConnection();
        webConnection.setResponse(URL_FIRST, firstContent);
        webConnection.setResponse(URL_SECOND, secondContent);

        client.setWebConnection(webConnection);

        final HtmlPage firstPage = client.getPage(URL_FIRST);
        final HtmlForm f = firstPage.getForms().get(0);
        final HtmlFileInput fileInput = f.getInputByName("image");

        final URL fileURL = getClass().getClassLoader().getResource("testfiles/empty.png");

        fileInput.setValueAttribute(fileURL.toExternalForm());
        f.getInputByName("mysubmit").click();
        final KeyDataPair pair = (KeyDataPair) webConnection.getLastParameters().get(0);
        assertNotNull(pair.getFile());
        Assert.assertFalse("Content type: " + pair.getMimeType(), "text/webtest".equals(pair.getMimeType()));

        fileInput.setContentType("text/webtest");
        f.getInputByName("mysubmit").click();
        final KeyDataPair pair2 = (KeyDataPair) webConnection.getLastParameters().get(0);
        assertNotNull(pair2.getFile());
        assertEquals("text/webtest", pair2.getMimeType());
    }

    /**
     * Test HttpClient for uploading a file with non-ASCII name, if it works it means HttpClient has fixed its bug.
     *
     * Test for http://issues.apache.org/jira/browse/HTTPCLIENT-293,
     * which is related to http://sourceforge.net/p/htmlunit/bugs/535/
     *
     * @throws Exception if the test fails
     */
    @Test
    @Browsers(NONE)
    public void uploadFileWithNonASCIIName_HttpClient() throws Exception {
        final String filename = "\u6A94\u6848\uD30C\uC77C\u30D5\u30A1\u30A4\u30EB\u0645\u0644\u0641.txt";
        final String path = getClass().getClassLoader().getResource(filename).toExternalForm();
        final File file = new File(new URI(path));
        assertTrue(file.exists());

        final Map<String, Class<? extends Servlet>> servlets = new HashMap<String, Class<? extends Servlet>>();
        servlets.put("/upload2", Upload2Servlet.class);

        startWebServer("./", null, servlets);
        final HttpPost filePost = new HttpPost("http://localhost:" + PORT + "/upload2");

        final MultipartEntityBuilder builder = MultipartEntityBuilder.create();
        builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE)
            .setCharset(Charset.forName("UTF-8"));
        builder.addPart("myInput", new FileBody(file, ContentType.APPLICATION_OCTET_STREAM));

        filePost.setEntity(builder.build());

        final HttpClientBuilder clientBuilder = HttpClientBuilder.create();
        final HttpResponse httpResponse = clientBuilder.build().execute(filePost);

        InputStream content = null;
        try {
            content = httpResponse.getEntity().getContent();
            final String response = new String(IOUtils.toByteArray(content));
            //this is the value with ASCII encoding
            assertFalse("3F 3F 3F 3F 3F 3F 3F 3F 3F 3F 3F 2E 74 78 74 <br>myInput".equals(response));
        }
        finally {
            IOUtils.closeQuietly(content);
        }
    }

    /**
     * Test uploading a file with non-ASCII name.
     *
     * Test for http://sourceforge.net/p/htmlunit/bugs/535/
     *
     * @throws Exception if the test fails
     */
    @Test
    public void testUploadFileWithNonASCIIName() throws Exception {
        final Map<String, Class<? extends Servlet>> servlets = new HashMap<String, Class<? extends Servlet>>();
        servlets.put("/upload1", Upload1Servlet.class);
        servlets.put("/upload2", Upload2Servlet.class);
        startWebServer("./", null, servlets);

        final String filename = "\u6A94\u6848\uD30C\uC77C\u30D5\u30A1\u30A4\u30EB\u0645\u0644\u0641.txt";
        final String path = getClass().getClassLoader().getResource(filename).toExternalForm();
        final File file = new File(new URI(path));
        assertTrue(file.exists());

        final WebClient client = getWebClient();
        final HtmlPage firstPage = client.getPage("http://localhost:" + PORT + "/upload1");

        final HtmlForm form = firstPage.getForms().get(0);
        final HtmlFileInput fileInput = form.getInputByName("myInput");
        fileInput.setValueAttribute(path);

        final HtmlSubmitInput submitInput = form.getInputByValue("Upload");
        final HtmlPage secondPage = submitInput.click();

        final String response = secondPage.getWebResponse().getContentAsString();

        //this is the value with UTF-8 encoding
        final String expectedResponse = "6A94 6848 D30C C77C 30D5 30A1 30A4 30EB 645 644 641 2E 74 78 74 <br>myInput";

        assertTrue("Invalid Response: " + response, response.contains(expectedResponse));

        if (getBrowserVersion().isIE()) {
            assertTrue(expectedResponse.length() < response.length());
        }
        else {
            assertEquals(expectedResponse.length(), response.length());
        }
    }

    /**
     * Servlet for '/upload1'.
     */
    public static class Upload1Servlet extends HttpServlet {

        /**
         * {@inheritDoc}
         */
        @Override
        protected void doGet(final HttpServletRequest request, final HttpServletResponse response)
            throws ServletException, IOException {
            response.setCharacterEncoding("UTF-8");
            response.setContentType("text/html");
            response.getWriter().write("<html>"
                + "<body><form action='upload2' method='post' enctype='multipart/form-data'>\n"
                + "Name: <input name='myInput' type='file'><br>\n"
                + "Name 2 (should stay empty): <input name='myInput2' type='file'><br>\n"
                + "<input type='submit' value='Upload' id='mySubmit'>\n"
                + "</form></body></html>\n");
        }
    }

    /**
     * Servlet for '/upload2'.
     */
    public static class Upload2Servlet extends HttpServlet {

        /**
         * {@inheritDoc}
         */
        @Override
        protected void doPost(final HttpServletRequest request, final HttpServletResponse response)
            throws ServletException, IOException {
            request.setCharacterEncoding("UTF-8");
            response.setContentType("text/html");
            final Writer writer = response.getWriter();
            if (ServletFileUpload.isMultipartContent(request)) {
                try {
                    final ServletFileUpload upload = new ServletFileUpload(new DiskFileItemFactory());
                    for (final FileItem item : upload.parseRequest(request)) {
                        if ("myInput".equals(item.getFieldName())) {
                            final String path = item.getName();
                            for (final char ch : path.toCharArray()) {
                                writer.write(Integer.toHexString(ch).toUpperCase() + " ");
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
}
