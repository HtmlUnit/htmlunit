/*
 * Copyright (c) 2002-2013 Gargoyle Software Inc.
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
import java.io.InputStream;
import java.io.Writer;
import java.util.HashMap;
import java.util.List;
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
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.WebDriverTestCase;
import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;

/**
 * Tests for {@link HtmlFileInput}.
 *
 * @version $Revision$
 * @author Ahmed Ashour
 * @author Ronald Brill
 */
@RunWith(BrowserRunner.class)
public class HtmlFileInput2Test extends WebDriverTestCase {

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void contentType() throws Exception {
        final Map<String, Class<? extends Servlet>> servlets = new HashMap<String, Class<? extends Servlet>>();
        servlets.put("/upload1", Upload1Servlet.class);
        servlets.put("/upload2", ContentTypeUpload2Servlet.class);
        startWebServer("./", new String[0], servlets);

        final WebDriver driver = getWebDriver();
        driver.get("http://localhost:" + PORT + "/upload1");
        String path = getClass().getClassLoader().getResource("realm.properties").toExternalForm();
        if (driver instanceof InternetExplorerDriver) {
            path = path.substring(path.indexOf('/') + 1).replace('/', '\\');
        }
        driver.findElement(By.name("myInput")).sendKeys(path);
        driver.findElement(By.id("mySubmit")).click();
        assertTrue(driver.getPageSource().contains("CONTENT_TYPE:application/octet-stream"));
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
    public static class ContentTypeUpload2Servlet extends HttpServlet {

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
            writer.close();
        }
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void boundary() throws Exception {
        final Map<String, Class<? extends Servlet>> servlets = new HashMap<String, Class<? extends Servlet>>();
        servlets.put("/upload1", Upload1Servlet.class);
        servlets.put("/upload2", PrintRequestServlet.class);
        startWebServer("./", new String[0], servlets);

        final WebDriver driver = getWebDriver();
        driver.get("http://localhost:" + PORT + "/upload1");
        String path = getClass().getClassLoader().getResource("realm.properties").toExternalForm();
        if (driver instanceof InternetExplorerDriver) {
            path = path.substring(path.indexOf('/') + 1).replace('/', '\\');
        }
        driver.findElement(By.name("myInput")).sendKeys(path);
        driver.findElement(By.id("mySubmit")).click();
        assertTrue(driver.getPageSource().contains("-----------------------------"));
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
            request.setCharacterEncoding("UTF-8");
            response.setContentType("text/html");
            final Writer writer = response.getWriter();
            final InputStream in = request.getInputStream();
            int i;
            while ((i = in.read()) != -1) {
                writer.write(i);
            }
            writer.close();
        }
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void contentTypeHeader() throws Exception {
        final Map<String, Class<? extends Servlet>> servlets = new HashMap<String, Class<? extends Servlet>>();
        servlets.put("/upload1", Upload1Servlet.class);
        servlets.put("/upload2", ContentTypeHeaderUpload2Servlet.class);
        startWebServer("./", new String[0], servlets);

        final WebDriver driver = getWebDriver();
        driver.get("http://localhost:" + PORT + "/upload1");
        String path = getClass().getClassLoader().getResource("realm.properties").toExternalForm();
        if (driver instanceof InternetExplorerDriver) {
            path = path.substring(path.indexOf('/') + 1).replace('/', '\\');
        }
        driver.findElement(By.name("myInput")).sendKeys(path);
        driver.findElement(By.id("mySubmit")).click();
        final String source = driver.getPageSource();
        assertTrue(source.contains("CONTENT_TYPE:"));
        assertFalse(source.contains("charset"));
    }

    /**
     * Servlet for '/upload2'.
     */
    public static class ContentTypeHeaderUpload2Servlet extends HttpServlet {

        /**
         * {@inheritDoc}
         */
        @Override
        protected void doPost(final HttpServletRequest request, final HttpServletResponse response)
            throws ServletException, IOException {
            request.setCharacterEncoding("UTF-8");
            response.setContentType("text/html");
            final Writer writer = response.getWriter();
            writer.write("CONTENT_TYPE:" + request.getContentType());
            writer.close();
        }
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void empty() throws Exception {
        final Map<String, Class<? extends Servlet>> servlets = new HashMap<String, Class<? extends Servlet>>();
        servlets.put("/upload1", Upload1Servlet.class);
        servlets.put("/upload2", PrintRequestServlet.class);
        startWebServer("./", new String[0], servlets);

        final WebDriver driver = getWebDriver();
        driver.get("http://localhost:" + PORT + "/upload1");
        driver.findElement(By.id("mySubmit")).click();

        assertTrue(driver.getPageSource()
                .contains("Content-Disposition: form-data; name=\"myInput\"; filename=\"\""));
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void realFile() throws Exception {
        final Map<String, Class<? extends Servlet>> servlets = new HashMap<String, Class<? extends Servlet>>();
        servlets.put("/upload1", Upload1Servlet.class);
        servlets.put("/upload2", PrintRequestServlet.class);
        startWebServer("./", new String[0], servlets);

        final WebDriver driver = getWebDriver();
        driver.get("http://localhost:" + PORT + "/upload1");
        String path = getClass().getClassLoader().getResource("realm.properties").toExternalForm();
        if (driver instanceof InternetExplorerDriver) {
            path = path.substring(path.indexOf('/') + 1).replace('/', '\\');
        }
        driver.findElement(By.name("myInput")).sendKeys(path);
        driver.findElement(By.id("mySubmit")).click();

        if (getBrowserVersion().isIE()) {
            final Pattern pattern = Pattern
                .compile("Content-Disposition: form-data; name=\"myInput\";"
                        + " filename=\".*test-classes[\\\\/]realm\\.properties\"");
            final Matcher matcher = pattern.matcher(driver.getPageSource());
            assertTrue(matcher.find());
            return;
        }

        // all other browsers
        assertTrue(driver.getPageSource()
                .contains("Content-Disposition: form-data; name=\"myInput\"; filename=\"realm.properties\""));
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void chunked() throws Exception {
        final Map<String, Class<? extends Servlet>> servlets = new HashMap<String, Class<? extends Servlet>>();
        servlets.put("/upload1", Upload1Servlet.class);
        servlets.put("/upload2", ChunkedUpload2Servlet.class);
        startWebServer("./", new String[0], servlets);

        final WebDriver driver = getWebDriver();
        driver.get("http://localhost:" + PORT + "/upload1");
        driver.findElement(By.id("mySubmit")).click();
        assertFalse(driver.getPageSource().contains("chunked"));
    }

    /**
     * Servlet for '/upload2'.
     */
    public static class ChunkedUpload2Servlet extends HttpServlet {
        /**
         * {@inheritDoc}
         */
        @Override
        protected void doPost(final HttpServletRequest request, final HttpServletResponse response)
            throws ServletException, IOException {
            request.setCharacterEncoding("UTF-8");
            response.setContentType("text/html");
            final Writer writer = response.getWriter();
            writer.write("TRANSFER_ENCODING:" + request.getHeader("TRANSFER-ENCODING"));
            writer.close();
        }
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({ "-initial", "-default" })
    public void defaultValue() throws Exception {
        final String html = "<!DOCTYPE HTML>\n<html><head><title>foo</title>\n"
            + "<script>\n"
            + "  function test() {\n"
            + "    var file = document.getElementById('testId');\n"
            + "    alert(file.value + '-' + file.defaultValue);\n"

            + "    file.defaultValue = 'default';\n"
            + "    alert(file.value + '-' + file.defaultValue);\n"
            + "  }\n"
            + "</script>\n"
            + "</head><body onload='test()'>\n"
            + "<form>\n"
            + "  <input type='file' id='testId' value='initial'>\n"
            + "</form>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }
}
