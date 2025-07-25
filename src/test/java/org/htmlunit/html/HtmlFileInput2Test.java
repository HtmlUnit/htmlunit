/*
 * Copyright (c) 2002-2025 Gargoyle Software Inc.
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

import static java.nio.charset.StandardCharsets.UTF_8;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.Writer;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URI;
import java.net.URL;
import java.net.URLDecoder;
import java.text.Normalizer;
import java.text.Normalizer.Form;
import java.util.HashMap;
import java.util.Locale;
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
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClientBuilder;
import org.htmlunit.HttpWebConnection;
import org.htmlunit.MockWebConnection;
import org.htmlunit.WebClient;
import org.htmlunit.WebRequest;
import org.htmlunit.WebServerTestCase;
import org.htmlunit.junit.annotation.Alerts;
import org.htmlunit.util.KeyDataPair;
import org.htmlunit.util.MimeType;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link HtmlFileInput}.
 *
 * @author Marc Guillemot
 * @author Ahmed Ashour
 * @author Ronald Brill
 * @author Frank Danek
 */
public class HtmlFileInput2Test extends WebServerTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void fileInput() throws Exception {
        final URL fileURL = getClass().getClassLoader().getResource("testfiles/tiny-png.img");
        final File file = new File(fileURL.toURI());
        assertTrue("File '" + file.getAbsolutePath() + "' does not exist", file.exists());

        testFileInput(file);

        String path = fileURL.getPath();
        if (path.startsWith("file:")) {
            path = path.substring("file:".length());
        }
        while (path.startsWith("/")) {
            path = path.substring(1);
        }
        if (System.getProperty("os.name").toLowerCase(Locale.ROOT).contains("windows")) {
            testFileInput(new File(URLDecoder.decode(path.replace('/', '\\'), UTF_8.name())));
        }
        testFileInput(new File("file:/" + path));
        testFileInput(new File("file://" + path));
        testFileInput(new File("file:///" + path));
    }

    private void testFileInput(final File file) throws Exception {
        final String firstContent = DOCTYPE_HTML
            + "<html><head></head><body>\n"
            + "<form enctype='multipart/form-data' action='" + URL_SECOND + "' method='POST'>\n"
            + "  <input type='file' name='image'>\n"
            + "  <input type='submit' id='clickMe'>\n"
            + "</form>\n"
            + "</body>\n"
            + "</html>";
        final String secondContent = DOCTYPE_HTML + "<html><head><title>second</title></head></html>";
        final WebClient client = getWebClient();

        final MockWebConnection webConnection = new MockWebConnection();
        webConnection.setResponse(URL_FIRST, firstContent);
        webConnection.setResponse(URL_SECOND, secondContent);

        client.setWebConnection(webConnection);

        final HtmlPage firstPage = client.getPage(URL_FIRST);
        final HtmlForm f = firstPage.getForms().get(0);
        final HtmlFileInput fileInput = f.getInputByName("image");
        fileInput.setFiles(file);
        firstPage.getHtmlElementById("clickMe").click();
        final KeyDataPair pair = (KeyDataPair) webConnection.getLastParameters().get(0);
        assertNotNull(pair.getFile());
        assertTrue(pair.getFile().length() != 0);
    }

    /**
     * Tests setValueAttribute method.
     * @throws Exception if the test fails
     */
    @Test
    public void setValueAttributeAndSetDataDummyFile() throws Exception {
        final String firstContent = DOCTYPE_HTML
            + "<html><head></head><body>\n"
            + "<form enctype='multipart/form-data' action='" + URL_SECOND + "' method='POST'>\n"
            + "  <input type='file' name='image'>\n"
            + "  <input type='submit' id='mySubmit'>\n"
            + "</form>\n"
            + "</body>\n"
            + "</html>";
        final String secondContent = DOCTYPE_HTML + "<html><head><title>second</title></head></html>";
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

        assertNull(pair.getData());

        final HttpEntity httpEntity = post(client, webConnection);

        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            httpEntity.writeTo(out);

            assertFalse(out.toString().contains("dummy.txt"));
        }
    }

    /**
     * Tests setData method.
     * @throws Exception if the test fails
     */
    @Test
    public void setValueAndSetDataDummyFile() throws Exception {
        final String firstContent = DOCTYPE_HTML
            + "<html><head></head><body>\n"
            + "<form enctype='multipart/form-data' action='" + URL_SECOND + "' method='POST'>\n"
            + "  <input type='file' name='image'>\n"
            + "  <input type='submit' id='mySubmit'>\n"
            + "</form>\n"
            + "</body>\n"
            + "</html>";
        final String secondContent = DOCTYPE_HTML + "<html><head><title>second</title></head></html>";
        final WebClient client = getWebClient();

        final MockWebConnection webConnection = new MockWebConnection();
        webConnection.setResponse(URL_FIRST, firstContent);
        webConnection.setResponse(URL_SECOND, secondContent);

        client.setWebConnection(webConnection);

        final HtmlPage firstPage = client.getPage(URL_FIRST);
        final HtmlForm f = firstPage.getForms().get(0);
        final HtmlFileInput fileInput = f.getInputByName("image");
        fileInput.setValue("dummy.txt");
        fileInput.setContentType("text/csv");
        fileInput.setData("My file data".getBytes());
        firstPage.getHtmlElementById("mySubmit").click();
        final KeyDataPair pair = (KeyDataPair) webConnection.getLastParameters().get(0);

        assertNotNull(pair.getData());
        assertTrue(pair.getData().length > 10);

        final HttpEntity httpEntity = post(client, webConnection);

        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            httpEntity.writeTo(out);

            assertTrue(out.toString().contains("dummy.txt"));
        }
    }

    /**
     * Tests setValueAttribute method.
     * @throws Exception if the test fails
     */
    @Test
    public void setValueAttributeAndSetDataRealFile() throws Exception {
        final String firstContent = DOCTYPE_HTML
            + "<html><head></head><body>\n"
            + "<form enctype='multipart/form-data' action='" + URL_SECOND + "' method='POST'>\n"
            + "  <input type='file' name='image'>\n"
            + "  <input type='submit' id='mySubmit'>\n"
            + "</form>\n"
            + "</body>\n"
            + "</html>";
        final String secondContent = DOCTYPE_HTML + "<html><head><title>second</title></head></html>";
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

        assertNull(pair.getData());

        final HttpEntity httpEntity = post(client, webConnection);

        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            httpEntity.writeTo(out);

            assertFalse(out.toString()
                    .contains("Content-Disposition: form-data; name=\"image\"; filename=\"tiny-png.img\""));
        }
    }

    /**
     * Tests setData method.
     * @throws Exception if the test fails
     */
    @Test
    public void setValueAndSetDataRealFile() throws Exception {
        final String firstContent = DOCTYPE_HTML
            + "<html><head></head><body>\n"
            + "<form enctype='multipart/form-data' action='" + URL_SECOND + "' method='POST'>\n"
            + "  <input type='file' name='image'>\n"
            + "  <input type='submit' id='mySubmit'>\n"
            + "</form>\n"
            + "</body>\n"
            + "</html>";
        final String secondContent = DOCTYPE_HTML + "<html><head><title>second</title></head></html>";
        final WebClient client = getWebClient();

        final MockWebConnection webConnection = new MockWebConnection();
        webConnection.setResponse(URL_FIRST, firstContent);
        webConnection.setResponse(URL_SECOND, secondContent);

        client.setWebConnection(webConnection);

        final HtmlPage firstPage = client.getPage(URL_FIRST);
        final HtmlForm f = firstPage.getForms().get(0);
        final HtmlFileInput fileInput = f.getInputByName("image");
        final String path = getClass().getClassLoader().getResource("testfiles/" + "tiny-png.img").toExternalForm();
        fileInput.setValue(path);
        fileInput.setData("My file data".getBytes());
        firstPage.getHtmlElementById("mySubmit").click();
        final KeyDataPair pair = (KeyDataPair) webConnection.getLastParameters().get(0);

        assertNotNull(pair.getData());
        assertTrue(pair.getData().length > 10);

        final HttpEntity httpEntity = post(client, webConnection);

        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            httpEntity.writeTo(out);

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
        final String firstContent = DOCTYPE_HTML
            + "<html><head></head><body>\n"
            + "<form enctype='multipart/form-data' action='" + URL_SECOND + "' method='POST'>\n"
            + "  <input type='file' name='image'>\n"
            + "  <input type='submit' id='mySubmit'>\n"
            + "</form>\n"
            + "</body>\n"
            + "</html>";
        final String secondContent = DOCTYPE_HTML + "<html><head><title>second</title></head></html>";
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

        final HttpEntity httpEntity = post(client, webConnection);

        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            httpEntity.writeTo(out);

            assertTrue(out.toString()
                    .contains("Content-Disposition: form-data; name=\"image\"; filename=\"\""));
        }
    }

    /**
     * Helper that does some nasty magic.
     */
    private static HttpEntity post(final WebClient client,
            final MockWebConnection webConnection)
            throws NoSuchMethodException, IllegalAccessException,
            InvocationTargetException {
        final Method makeHttpMethod = HttpWebConnection.class.getDeclaredMethod("makeHttpMethod",
                WebRequest.class, HttpClientBuilder.class);
        makeHttpMethod.setAccessible(true);

        final HttpWebConnection con = new HttpWebConnection(client);

        final Method getHttpClientBuilderMethod = HttpWebConnection.class.getDeclaredMethod("getHttpClientBuilder");
        getHttpClientBuilderMethod.setAccessible(true);
        final HttpClientBuilder builder = (HttpClientBuilder) getHttpClientBuilderMethod.invoke(con);

        final HttpPost httpPost = (HttpPost) makeHttpMethod.invoke(con, webConnection.getLastWebRequest(), builder);
        final HttpEntity httpEntity = httpPost.getEntity();
        return httpEntity;
    }

    /**
     * Verifies that content is provided for a not filled file input.
     * @throws Exception if the test fails
     */
    @Test
    public void emptyField() throws Exception {
        final String firstContent = DOCTYPE_HTML
            + "<html><head></head><body>\n"
            + "<form enctype='multipart/form-data' action='" + URL_SECOND + "' method='POST'>\n"
            + "  <input type='file' name='image' />\n"
            + "  <input type='submit' id='clickMe'>\n"
            + "</form>\n"
            + "</body>\n"
            + "</html>";
        final String secondContent = DOCTYPE_HTML + "<html><head><title>second</title></head></html>";
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
    public void contentType() throws Exception {
        final String firstContent = DOCTYPE_HTML
            + "<html><head></head><body>\n"
            + "<form enctype='multipart/form-data' action='" + URL_SECOND + "' method='POST'>\n"
            + "  <input type='file' name='image' />\n"
            + "  <input type='submit' name='mysubmit'/>\n"
            + "</form>\n"
            + "</body>\n"
            + "</html>";
        final String secondContent = DOCTYPE_HTML + "<html><head><title>second</title></head></html>";
        final WebClient client = getWebClient();

        final MockWebConnection webConnection = new MockWebConnection();
        webConnection.setResponse(URL_FIRST, firstContent);
        webConnection.setResponse(URL_SECOND, secondContent);

        client.setWebConnection(webConnection);

        final HtmlPage firstPage = client.getPage(URL_FIRST);
        final HtmlForm f = firstPage.getForms().get(0);
        final HtmlFileInput fileInput = f.getInputByName("image");

        final URL fileURL = getClass().getClassLoader().getResource("testfiles/empty.png");
        final File file = new File(fileURL.toURI());
        assertTrue("File '" + file.getAbsolutePath() + "' does not exist", file.exists());

        fileInput.setFiles(file);
        f.getInputByName("mysubmit").click();

        assertEquals(2, webConnection.getLastParameters().size());
        KeyDataPair pair = (KeyDataPair) webConnection.getLastParameters().get(0);
        if ("mysubmit".equals(pair.getName())) {
            pair = (KeyDataPair) webConnection.getLastParameters().get(1);
        }
        assertNotNull(pair.getFile());
        assertFalse("Content type: " + pair.getMimeType(), "text/webtest".equals(pair.getMimeType()));

        fileInput.setContentType("text/webtest");
        f.getInputByName("mysubmit").click();

        assertEquals(2, webConnection.getLastParameters().size());
        KeyDataPair pair2 = (KeyDataPair) webConnection.getLastParameters().get(0);
        if ("mysubmit".equals(pair2.getName())) {
            pair2 = (KeyDataPair) webConnection.getLastParameters().get(1);
        }
        assertNotNull(pair2.getFile());
        assertEquals("text/webtest", pair2.getMimeType());
    }

    /**
     * Test uploading a file with non-ASCII name.
     *
     * Test for http://sourceforge.net/p/htmlunit/bugs/535/
     *
     * @throws Exception if the test fails
     */
    @Test
    public void uploadFileWithNonASCIIName() throws Exception {
        final Map<String, Class<? extends Servlet>> servlets = new HashMap<>();
        servlets.put("/upload1", Upload1Servlet.class);
        servlets.put("/upload2", Upload2Servlet.class);
        startWebServer("./", null, servlets);

        final String filename = "\u6A94\u6848\uD30C\uC77C\u30D5\u30A1\u30A4\u30EB\u0645\u0644\u0641.txt";
        final URL fileURL = getClass().getClassLoader().getResource(filename);
        final File file = new File(fileURL.toURI());
        assertTrue("File '" + file.getAbsolutePath() + "' does not exist", file.exists());

        final WebClient client = getWebClient();
        final HtmlPage firstPage = client.getPage(URL_FIRST + "upload1");

        final HtmlForm form = firstPage.getForms().get(0);
        final HtmlFileInput fileInput = form.getInputByName("myInput");
        fileInput.setFiles(file);

        final HtmlSubmitInput submitInput = form.getInputByValue("Upload");
        final HtmlPage secondPage = submitInput.click();

        final String response = secondPage.getWebResponse().getContentAsString();

        //this is the value with UTF-8 encoding
        final String expectedResponse = "6A94 6848 D30C C77C 30D5 30A1 30A4 30EB 645 644 641 2E 74 78 74 <br>myInput";

        assertTrue("Invalid Response: " + response, response.endsWith(expectedResponse));
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
            response.setCharacterEncoding(UTF_8.name());
            response.setContentType(MimeType.TEXT_HTML);
            response.getWriter().write("<html>\n"
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
            request.setCharacterEncoding(UTF_8.name());
            response.setContentType(MimeType.TEXT_HTML);
            final Writer writer = response.getWriter();
            if (ServletFileUpload.isMultipartContent(request)) {
                try {
                    final ServletFileUpload upload = new ServletFileUpload(new DiskFileItemFactory());
                    for (final FileItem item : upload.parseRequest(request)) {
                        if ("myInput".equals(item.getFieldName())) {
                            final String path = item.getName();
                            for (final char ch : path.toCharArray()) {
                                writer.write(Integer.toHexString(ch).toUpperCase(Locale.ROOT) + " ");
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
        }
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void mutiple() throws Exception {
        final Map<String, Class<? extends Servlet>> servlets = new HashMap<>();
        servlets.put("/upload1", Multiple1Servlet.class);
        servlets.put("/upload2", PrintRequestServlet.class);
        startWebServer("./", null, servlets);

        final String filename1 = "HtmlFileInputTest_one.txt";
        final String path1 = getClass().getResource(filename1).toExternalForm();
        final File file1 = new File(new URI(path1));
        assertTrue(file1.exists());

        final String filename2 = "HtmlFileInputTest_two.txt";
        final String path2 = getClass().getResource(filename2).toExternalForm();
        final File file2 = new File(new URI(path2));
        assertTrue(file2.exists());

        final WebClient client = getWebClient();
        final HtmlPage firstPage = client.getPage(URL_FIRST + "upload1");

        final HtmlForm form = firstPage.getForms().get(0);
        final HtmlFileInput fileInput = form.getInputByName("myInput");
        fileInput.setFiles(file1, file2);

        final HtmlSubmitInput submitInput = form.getInputByValue("Upload");
        final HtmlPage secondPage = submitInput.click();

        final String response = secondPage.getWebResponse().getContentAsString();

        assertTrue(response.contains("HtmlFileInputTest_one.txt"));
        assertTrue(response.contains("First"));
        assertTrue(response.contains("HtmlFileInputTest_two.txt"));
        assertTrue(response.contains("Second"));
    }


    /**
     * @throws Exception if the test fails
     */
    @Test
    public void webkitdirectory() throws Exception {
        final Map<String, Class<? extends Servlet>> servlets = new HashMap<>();
        servlets.put("/upload1", MultipleWebkitdirectoryServlet.class);
        servlets.put("/upload2", PrintRequestServlet.class);
        startWebServer("./", null, servlets);

        final File dir = new File("src/test/resources/pjl-comp-filter");
        assertTrue(dir.exists());
        assertTrue(dir.isDirectory());

        final WebClient client = getWebClient();
        final HtmlPage firstPage = client.getPage(URL_FIRST + "upload1");

        final HtmlForm form = firstPage.getForms().get(0);
        final HtmlFileInput fileInput = form.getInputByName("myInput");
        fileInput.setDirectory(dir);

        final HtmlSubmitInput submitInput = form.getInputByValue("Upload");
        final HtmlPage secondPage = submitInput.click();

        final String response = secondPage.getWebResponse().getContentAsString();

        assertTrue(response.contains("index.html"));
        assertTrue(response.contains("web.xml"));
        assertTrue(response.contains("pjl-comp-filter-1.8.1.jar"));
    }

    /**
     * Servlet for '/upload1'.
     */
    public static class Multiple1Servlet extends HttpServlet {

        /**
         * {@inheritDoc}
         */
        @Override
        protected void doGet(final HttpServletRequest request, final HttpServletResponse response)
            throws ServletException, IOException {
            response.setCharacterEncoding(UTF_8.name());
            response.setContentType(MimeType.TEXT_HTML);
            response.getWriter().write("<html>\n"
                + "<body><form action='upload2' method='post' enctype='multipart/form-data'>\n"
                + "Name: <input name='myInput' type='file' multiple><br>\n"
                + "<input type='submit' value='Upload' id='mySubmit'>\n"
                + "</form></body></html>\n");
        }
    }

    /**
     * Servlet for '/upload1'.
     */
    public static class MultipleWebkitdirectoryServlet extends HttpServlet {

        /**
         * {@inheritDoc}
         */
        @Override
        protected void doGet(final HttpServletRequest request, final HttpServletResponse response)
            throws ServletException, IOException {
            response.setCharacterEncoding(UTF_8.name());
            response.setContentType(MimeType.TEXT_HTML);
            response.getWriter().write("<html>\n"
                + "<body><form action='upload2' method='post' enctype='multipart/form-data'>\n"
                + "Name: <input name='myInput' type='file' multiple webkitdirectory><br>\n"
                + "<input type='submit' value='Upload' id='mySubmit'>\n"
                + "</form></body></html>\n");
        }
    }

    /**
     * Prints request content to the response.
     */
    public static class PrintRequestServlet extends HttpServlet {

        /**
         * {@inheritDoc}
         */
        @Override
        protected void doPost(final HttpServletRequest request, final HttpServletResponse response)
            throws ServletException, IOException {
            request.setCharacterEncoding(UTF_8.name());
            response.setContentType(MimeType.TEXT_HTML);
            final Writer writer = response.getWriter();
            final BufferedReader reader = request.getReader();
            String line;
            while ((line = reader.readLine()) != null) {
                final String normalized = Normalizer.normalize(line, Form.NFD);
                writer.write(normalized);
            }
        }
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("foo, change")
    public void onchangeMultiple() throws Exception {
        final String html = DOCTYPE_HTML
              + "<html>\n"
              + "<head>\n"
              + "</head>\n"
              + "<body>\n"
              + "  <input type='file' id='f' value='Hello world' multiple"
              + "      onChange='alert(\"foo\");alert(event.type);'>\n"
              + "  <button id='clickMe' onclick='test()'>Click Me</button>\n"
              + "</body></html>";

        final File pom = new File("pom.xml");
        final File license = new File("LICENSE.txt");

        final HtmlPage page = loadPage(html);
        ((HtmlFileInput) page.getElementById("f")).setFiles(pom, license);
        Thread.sleep(100);
        assertEquals(getExpectedAlerts(), getCollectedAlerts(page));
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void clear() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html><head>\n"
                + "  <script>\n"
                + "    function test() {\n"
                + "      var f =  document.createElement('input');\n"
                + "      f.type='file';\n"
                + "      f.id='fileId';\n"
                + "      document.body.appendChild(f);"

                + "      f.value='';\n"
                + "    }\n"
                + "  </script>\n"
                + "</head>\n"
                + "<body onload='test()'>\n"
                + "</body></html>";

        final HtmlPage page = loadPage(html);
        final HtmlFileInput file = page.<HtmlFileInput>getHtmlElementById("fileId");
        assertEquals(0, file.getFiles().length);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void clearFromJava() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html><head>\n"
                + "  <script>\n"
                + "    function test() {\n"
                + "      var f =  document.createElement('input');\n"
                + "      f.type='file';\n"
                + "      f.id='fileId';\n"
                + "      document.body.appendChild(f);"
                + "    }\n"
                + "  </script>\n"
                + "</head>\n"
                + "<body onload='test()'>\n"
                + "</body></html>";

        final HtmlPage page = loadPage(html);
        final HtmlFileInput file = page.<HtmlFileInput>getHtmlElementById("fileId");
        file.setValue("");
        assertEquals(0, file.getFiles().length);
    }
}
