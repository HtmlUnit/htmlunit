/*
 * Copyright (c) 2002-2022 Gargoyle Software Inc.
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
package com.gargoylesoftware.htmlunit.httpclient;

import static java.nio.charset.StandardCharsets.UTF_8;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Writer;
import java.net.URL;
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
import org.apache.commons.io.IOUtils;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.entity.mime.FileBody;
import org.apache.hc.client5.http.entity.mime.HttpMultipartMode;
import org.apache.hc.client5.http.entity.mime.MultipartEntityBuilder;
import org.apache.hc.client5.http.impl.classic.HttpClientBuilder;
import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.http.HttpEntityContainer;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.gargoylesoftware.htmlunit.WebServerTestCase;
import com.gargoylesoftware.htmlunit.html.HtmlFileInput;
import com.gargoylesoftware.htmlunit.junit.BrowserRunner;
import com.gargoylesoftware.htmlunit.util.MimeType;

/**
 * Tests for {@link HtmlFileInput}.
 *
 * @author Marc Guillemot
 * @author Ahmed Ashour
 * @author Ronald Brill
 * @author Frank Danek
 */
@RunWith(BrowserRunner.class)
public class HttpClientTest extends WebServerTestCase {

    /**
     * Servlet for '/upload'.
     */
    public static class UploadServlet extends HttpServlet {

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
     * Test HttpClient for uploading a file with non-ASCII name, if it works it means HttpClient has fixed its bug.
     *
     * Test for http://issues.apache.org/jira/browse/HTTPCLIENT-293,
     * which is related to http://sourceforge.net/p/htmlunit/bugs/535/
     *
     * @throws Exception if the test fails
     */
    @Test
    public void uploadFileWithNonASCIIName_HttpClient() throws Exception {
        final String filename = "\u6A94\u6848\uD30C\uC77C\u30D5\u30A1\u30A4\u30EB\u0645\u0644\u0641.txt";
        final URL fileURL = getClass().getClassLoader().getResource(filename);
        assertNotNull("Resource '" + filename + "' not found", fileURL);
        final File file = new File(fileURL.toURI());
        assertTrue("File '" + file.getAbsolutePath() + "' does not exist", file.exists());

        final Map<String, Class<? extends Servlet>> servlets = new HashMap<>();
        servlets.put("/upload2", UploadServlet.class);

        startWebServer("./", null, servlets);
        final HttpPost filePost = new HttpPost(URL_FIRST + "upload2");

        final MultipartEntityBuilder builder = MultipartEntityBuilder.create();
        builder.setMode(HttpMultipartMode.LEGACY).setCharset(UTF_8);
        builder.addPart("myInput", new FileBody(file, ContentType.APPLICATION_OCTET_STREAM));

        filePost.setEntity(builder.build());

        final HttpClientBuilder clientBuilder = HttpClientBuilder.create();
        final HttpEntityContainer httpResponse = clientBuilder.build().execute(filePost);

        try (InputStream content = httpResponse.getEntity().getContent()) {
            final String response = new String(IOUtils.toByteArray(content));
            //this is the value with ASCII encoding
            assertFalse("3F 3F 3F 3F 3F 3F 3F 3F 3F 3F 3F 2E 74 78 74 <br>myInput".equals(response));
        }
    }
}
