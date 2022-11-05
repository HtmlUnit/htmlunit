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
package com.gargoylesoftware.htmlunit.html;

import static java.nio.charset.StandardCharsets.UTF_8;

import java.io.File;
import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;
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
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;

import com.gargoylesoftware.htmlunit.WebDriverTestCase;
import com.gargoylesoftware.htmlunit.junit.BrowserRunner;
import com.gargoylesoftware.htmlunit.junit.BrowserRunner.Alerts;
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
public class HtmlFileInput3Test extends WebDriverTestCase {

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"CONTENT_TYPE:application/octet-stream", "charset"})
    public void contentTypeUnknown() throws Exception {
        contentType("unknown");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"CONTENT_TYPE:text/html", "charset"})
    public void contentTypeHtm() throws Exception {
        contentType("htm");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"CONTENT_TYPE:text/html", "charset"})
    public void contentTypeHtml() throws Exception {
        contentType("html");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"CONTENT_TYPE:text/css", "charset"})
    public void contentTypeCss() throws Exception {
        contentType("css");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"CONTENT_TYPE:text/xml", "charset"})
    public void contentTypeXml() throws Exception {
        contentType("xml");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"CONTENT_TYPE:image/gif", "charset"})
    public void contentTypeGif() throws Exception {
        contentType("gif");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"CONTENT_TYPE:image/jpeg", "charset"})
    public void contentTypeJpeg() throws Exception {
        contentType("jpeg");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"CONTENT_TYPE:image/jpeg", "charset"})
    public void contentTypeJpg() throws Exception {
        contentType("jpg");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"CONTENT_TYPE:image/png", "charset"})
    public void contentTypePng() throws Exception {
        contentType("png");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = {"CONTENT_TYPE:image/webp", "charset"},
            IE = {"CONTENT_TYPE:application/octet-stream", "charset"})
    public void contentTypeWebp() throws Exception {
        contentType("webp");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"CONTENT_TYPE:video/mp4", "charset"})
    public void contentTypeMp4() throws Exception {
        contentType("mp4");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"CONTENT_TYPE:video/mp4", "charset"})
    public void contentTypeM4v() throws Exception {
        contentType("m4v");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = {"CONTENT_TYPE:audio/mp4", "charset"},
            CHROME = {"CONTENT_TYPE:audio/x-m4a", "charset"},
            EDGE = {"CONTENT_TYPE:audio/x-m4a", "charset"})
    public void contentTypeM4a() throws Exception {
        contentType("m4a");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"CONTENT_TYPE:audio/mpeg", "charset"})
    public void contentTypeMp3() throws Exception {
        contentType("mp3");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"CONTENT_TYPE:video/ogg", "charset"})
    public void contentTypeOgv() throws Exception {
        contentType("ogv");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"CONTENT_TYPE:video/ogg", "charset"})
    public void contentTypeOgm() throws Exception {
        contentType("ogm");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = {"CONTENT_TYPE:audio/ogg", "charset"},
            FF = {"CONTENT_TYPE:video/ogg", "charset"},
            FF_ESR = {"CONTENT_TYPE:video/ogg", "charset"})
    public void contentTypeOgg() throws Exception {
        contentType("ogg");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"CONTENT_TYPE:audio/ogg", "charset"})
    public void contentTypeOga() throws Exception {
        contentType("oga");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"CONTENT_TYPE:audio/ogg", "charset"})
    public void contentTypeOpus() throws Exception {
        contentType("opus");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"CONTENT_TYPE:video/webm", "charset"})
    public void contentTypeWebm() throws Exception {
        contentType("webm");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"CONTENT_TYPE:audio/wav", "charset"})
    public void contentTypeWav() throws Exception {
        contentType("wav");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = {"CONTENT_TYPE:audio/flac", "charset"},
            FF = {"CONTENT_TYPE:audio/x-flac", "charset"},
            FF_ESR = {"CONTENT_TYPE:audio/x-flac", "charset"},
            IE = {"CONTENT_TYPE:audio/x-flac", "charset"})
    public void contentTypeFlac() throws Exception {
        contentType("flac");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"CONTENT_TYPE:application/xhtml+xml", "charset"})
    public void contentTypeXhtml() throws Exception {
        contentType("xhtml");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"CONTENT_TYPE:application/xhtml+xml", "charset"})
    public void contentTypeXht() throws Exception {
        contentType("xht");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = {"CONTENT_TYPE:application/octet-stream", "charset"},
            CHROME = {"CONTENT_TYPE:application/xhtml+xml", "charset"},
            EDGE = {"CONTENT_TYPE:application/xhtml+xml", "charset"})
    public void contentTypeXhtm() throws Exception {
        contentType("xhtm");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = {"CONTENT_TYPE:text/plain", "charset"},
            IE = {"CONTENT_TYPE:application/octet-stream", "charset"})
    public void contentTypeText() throws Exception {
        contentType("text");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"CONTENT_TYPE:text/plain", "charset"})
    public void contentTypeTxt() throws Exception {
        contentType("txt");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"CONTENT_TYPE:text/html", "charset"})
    public void contentTypeCaseInsensitive() throws Exception {
        contentType("HtmL");
    }

    /**
     * Starts the web server.
     */
    @BeforeClass
    public static void before() {
        try {
            final Map<String, Class<? extends Servlet>> servlets = new HashMap<>();
            servlets.put("/upload1", Upload1Servlet.class);
            servlets.put("/upload2", ContentTypeUpload2Servlet.class);
            startWebServer("./", new String[0], servlets);
        }
        catch (final Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void contentType(final String extension) throws Exception {
        final WebDriver driver = getWebDriver();
        driver.get(URL_FIRST + "upload1");

        final File tmpFile = File.createTempFile("htmlunit-test", "." + extension);
        try {
            String path = tmpFile.getAbsolutePath();
            if (driver instanceof InternetExplorerDriver) {
                path = path.substring(path.indexOf('/') + 1).replace('/', '\\');
            }
            driver.findElement(By.name("myInput")).sendKeys(path);
            driver.findElement(By.id("mySubmit")).submit();
        }
        finally {
            assertTrue(tmpFile.delete());
        }

        final long maxWait = System.currentTimeMillis() + DEFAULT_WAIT_TIME;

        while (System.currentTimeMillis() < maxWait) {
            try {
                final String pageSource = driver.getPageSource();
                assertNotNull(pageSource);
                assertTrue(pageSource, pageSource.contains(getExpectedAlerts()[0]));
                assertFalse(pageSource, pageSource.contains(getExpectedAlerts()[1]));
                return;
            }
            catch (final AssertionError e) {
                // ignore and wait
            }
        }

        final String pageSource = driver.getPageSource();
        assertTrue("\"" + pageSource + "\" does not contain \""
                + getExpectedAlerts()[0] + "\"", pageSource.contains(getExpectedAlerts()[0]));
        assertFalse("\"" + pageSource + "\" contains \""
                + getExpectedAlerts()[0] + "\" but should not", pageSource.contains(getExpectedAlerts()[1]));
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
                + "<body>\n"
                + "<form action='upload2' method='post' enctype='multipart/form-data'>\n"
                + "Name: <input name='myInput' type='file'><br>\n"
                + "Name 2 (should stay empty): <input name='myInput2' type='file'><br>\n"
                + "<input type='submit' value='Upload' id='mySubmit'>\n"
                + "</form>\n"
                + "</body></html>\n");
        }
    }

    /**
     * Servlet for '/upload2'.
     */
    public static class ContentTypeUpload2Servlet extends HttpServlet {

        /**
         * {@inheritDoc}
         */
        @Override
        protected void doPost(final HttpServletRequest request, final HttpServletResponse response)
            throws ServletException, IOException {
            request.setCharacterEncoding(UTF_8.name());
            response.setContentType(MimeType.TEXT_HTML);
            try (Writer writer = response.getWriter()) {
                if (ServletFileUpload.isMultipartContent(request)) {
                    try {
                        final ServletFileUpload upload = new ServletFileUpload(new DiskFileItemFactory());
                        for (final FileItem item : upload.parseRequest(request)) {
                            if ("myInput".equals(item.getFieldName())) {
                                writer.write("CONTENT_TYPE:" + item.getContentType());
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
    }
}
