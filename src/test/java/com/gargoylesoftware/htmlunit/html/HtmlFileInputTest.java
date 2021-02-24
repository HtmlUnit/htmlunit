/*
 * Copyright (c) 2002-2021 Gargoyle Software Inc.
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
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;
import com.gargoylesoftware.htmlunit.HttpHeader;
import com.gargoylesoftware.htmlunit.WebDriverTestCase;
import com.gargoylesoftware.htmlunit.WebRequest;
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
public class HtmlFileInputTest extends WebDriverTestCase {

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
            FF78 = {"CONTENT_TYPE:video/ogg", "charset"})
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
            FF78 = {"CONTENT_TYPE:audio/x-flac", "charset"},
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

    private void contentType(final String extension) throws Exception {
        final Map<String, Class<? extends Servlet>> servlets = new HashMap<>();
        servlets.put("/upload1", Upload1Servlet.class);
        servlets.put("/upload2", ContentTypeUpload2Servlet.class);
        startWebServer("./", new String[0], servlets);

        final WebDriver driver = getWebDriver();
        driver.get(URL_FIRST + "upload1");

        final File tmpFile = File.createTempFile("htmlunit-test", "." + extension);
        try {
            String path = tmpFile.getAbsolutePath();
            if (driver instanceof InternetExplorerDriver || driver instanceof ChromeDriver) {
                path = path.substring(path.indexOf('/') + 1).replace('/', '\\');
            }
            driver.findElement(By.name("myInput")).sendKeys(path);
            driver.findElement(By.id("mySubmit")).click();
        }
        finally {
            assertTrue(tmpFile.delete());
        }

        final String pageSource = driver.getPageSource();
        assertTrue(pageSource, pageSource.contains(getExpectedAlerts()[0]));
        assertFalse(pageSource, pageSource.contains(getExpectedAlerts()[1]));
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

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void contentTypeHeader() throws Exception {
        final String htmlContent
            = "<html>\n"
                + "<body>\n"
                + "<form action='upload2' method='post' enctype='multipart/form-data'>\n"
                + "Name: <input name='myInput' type='file'><br>\n"
                + "Name 2 (should stay empty): <input name='myInput2' type='file'><br>\n"
                + "<input type='submit' value='Upload' id='mySubmit'>\n"
                + "</form>\n"
                + "</body></html>\n";
        getMockWebConnection().setDefaultResponse("Error: not found", 404, "Not Found", MimeType.TEXT_HTML);

        final WebDriver driver = loadPage2(htmlContent);
        String path = getClass().getClassLoader().getResource("realm.properties").toExternalForm();
        path = path.substring(path.indexOf('/') + 1).replace('/', '\\');

        driver.findElement(By.name("myInput")).sendKeys(path);
        driver.findElement(By.id("mySubmit")).click();

        final WebRequest request = getMockWebConnection().getLastWebRequest();
        final String contentType = request.getAdditionalHeaders().get(HttpHeader.CONTENT_TYPE);
        assertTrue(StringUtils.isNotBlank(contentType));
        assertFalse(StringUtils.containsIgnoreCase(contentType, "charset"));
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("Content-Disposition: form-data; name=\"myInput\"; filename=\"\"")
    public void empty() throws Exception {
        final String htmlContent
            = "<html>\n"
                + "<body>\n"
                + "<form action='upload2' method='post' enctype='multipart/form-data'>\n"
                + "Name: <input name='myInput' type='file'><br>\n"
                + "Name 2 (should stay empty): <input name='myInput2' type='file'><br>\n"
                + "<input type='submit' value='Upload' id='mySubmit'>\n"
                + "</form>\n"
                + "</body></html>\n";
        getMockWebConnection().setDefaultResponse("Error: not found", 404, "Not Found", MimeType.TEXT_HTML);

        final WebDriver driver = loadPage2(htmlContent);
        driver.findElement(By.id("mySubmit")).click();

        final String pageSource = getMockWebConnection().getLastWebRequest().getRequestBody();
        final Matcher matcher = Pattern.compile(getExpectedAlerts()[0]).matcher(pageSource);
        assertTrue(pageSource, matcher.find());
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("Content-Disposition: form-data; name=\"myInput\"; filename=\"realm.properties\"")
    // since 2.28
    // there is an option for IE, for local and trusted sites IE includes the file path
    // because we do not support any IE specific setting we do not send the filename as
    // done by the other browsers
    public void realFile() throws Exception {
        final String htmlContent
            = "<html>\n"
                + "<body>\n"
                + "<form action='upload2' method='post' enctype='multipart/form-data'>\n"
                + "Name: <input name='myInput' type='file'><br>\n"
                + "Name 2 (should stay empty): <input name='myInput2' type='file'><br>\n"
                + "<input type='submit' value='Upload' id='mySubmit'>\n"
                + "</form>\n"
                + "</body></html>\n";
        getMockWebConnection().setDefaultResponse("Error: not found", 404, "Not Found", MimeType.TEXT_HTML);

        final WebDriver driver = loadPage2(htmlContent);

        String path = getClass().getClassLoader().getResource("realm.properties").toExternalForm();
        path = path.substring(path.indexOf('/') + 1).replace('/', '\\');

        driver.findElement(By.name("myInput")).sendKeys(path);
        driver.findElement(By.id("mySubmit")).click();

        final String pageSource = getMockWebConnection().getLastWebRequest().getRequestBody();
        final Matcher matcher = Pattern.compile(getExpectedAlerts()[0]).matcher(pageSource);
        assertTrue(pageSource, matcher.find());
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void chunked() throws Exception {
        final String htmlContent
            = "<html>\n"
                + "<body>\n"
                + "<form action='upload2' method='post' enctype='multipart/form-data'>\n"
                + "Name: <input name='myInput' type='file'><br>\n"
                + "Name 2 (should stay empty): <input name='myInput2' type='file'><br>\n"
                + "<input type='submit' value='Upload' id='mySubmit'>\n"
                + "</form>\n"
                + "</body></html>\n";
        getMockWebConnection().setDefaultResponse("Error: not found", 404, "Not Found", MimeType.TEXT_HTML);

        final WebDriver driver = loadPage2(htmlContent);

        driver.findElement(By.id("mySubmit")).click();

        final String pageSource = getMockWebConnection().getLastWebRequest().getRequestBody();
        assertFalse(pageSource.contains("chunked"));
    }

    /**
     * Verifies that a asText() returns an empty string.
     * @throws Exception if the test fails
     */
    @Test
    public void asText() throws Exception {
        final String htmlContent
            = "<html><head><title>foo</title></head><body>\n"
            + "<form id='form1'>\n"
            + "  <input type='file' name='foo' id='foo' value='bla'>\n"
            + "</form></body></html>";

        final WebDriver driver = loadPage2(htmlContent);

        final WebElement input = driver.findElement(By.id("foo"));
        assertEquals("", input.getText());
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = {"exception", "-Hello world-Hello world"},
            IE = "-Hello world-Hello world")
    public void setValueOnChange() throws Exception {
        final String html =
              "<html>\n"
              + "<head>\n"
              + "<script>\n"
              + "  function test() {\n"
              + "    var input = document.getElementById('f');\n"
              + "    try{\n"
              + "      input.value = 'HtmlUnit';\n"
              + "    } catch(e) { alert('exception'); }\n"
              + "    alert(input.value + '-' + input.defaultValue + '-' + input.getAttribute('value'));\n"
              + "  }\n"
              + "</script>\n"
              + "<body>\n"
              + "  <input type='file' id='f' value='Hello world'"
                    + " onChange='alert('foo');alert(event.type);'>\n"
              + "  <button id='b'>some button</button>\n"
              + "  <button id='set' onclick='test()'>setValue</button>\n"
              + "</body></html>";

        final WebDriver driver = loadPage2(html);
        driver.findElement(By.id("set")).click();

        assertEquals(getExpectedAlerts(), getCollectedAlerts(driver));

        // trigger lost focus
        driver.findElement(By.id("b")).click();
        assertTrue(getCollectedAlerts(driver, 1).isEmpty());
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("-HtmlUnit-HtmlUnit")
    public void setDefaultValueOnChange() throws Exception {
        final String html =
              "<html>\n"
              + "<head>\n"
              + "<script>\n"
              + "  function test() {\n"
              + "    var input = document.getElementById('f');\n"
              + "    try{\n"
              + "      input.defaultValue = 'HtmlUnit';\n"
              + "    } catch(e) { alert('exception'); }\n"
              + "    alert(input.value + '-' + input.defaultValue + '-' + input.getAttribute('value'));\n"
              + "  }\n"
              + "</script>\n"
              + "</head>\n"
              + "<body>\n"
              + "  <input type='file' id='f' value='Hello world'"
                    + " onChange='alert(\"foo\");alert(event.type);'>\n"
              + "  <button id='b'>some button</button>\n"
              + "  <button id='set' onclick='test()'>setValue</button>\n"
              + "</body></html>";

        final WebDriver driver = loadPage2(html);
        driver.findElement(By.id("set")).click();

        assertEquals(getExpectedAlerts(), getCollectedAlerts(driver));

        // trigger lost focus
        driver.findElement(By.id("b")).click();
        assertTrue(getCollectedAlerts(driver, 1).isEmpty());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"--null", "--null", "--null"})
    public void defaultValues() throws Exception {
        final String html = "<html><head><title>foo</title>\n"
            + "<script>\n"
            + "  function test() {\n"
            + "    var input = document.getElementById('file1');\n"
            + "    alert(input.value + '-' + input.defaultValue + '-' + input.getAttribute('value'));\n"

            + "    input = document.createElement('input');\n"
            + "    input.type = 'file';\n"
            + "    alert(input.value + '-' + input.defaultValue + '-' + input.getAttribute('value'));\n"

            + "    var builder = document.createElement('div');\n"
            + "    builder.innerHTML = '<input type=\"file\">';\n"
            + "    input = builder.firstChild;\n"
            + "    alert(input.value + '-' + input.defaultValue + '-' + input.getAttribute('value'));\n"
            + "  }\n"
            + "</script>\n"
            + "</head><body onload='test()'>\n"
            + "<form>\n"
            + "  <input type='file' id='file1'>\n"
            + "</form>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"--null", "--null", "--null"})
    public void defaultValuesAfterClone() throws Exception {
        final String html = "<html><head><title>foo</title>\n"
            + "<script>\n"
            + "  function test() {\n"
            + "    var input = document.getElementById('file1');\n"
            + "    input = input.cloneNode(false);\n"
            + "    alert(input.value + '-' + input.defaultValue + '-' + input.getAttribute('value'));\n"

            + "    input = document.createElement('input');\n"
            + "    input.type = 'file';\n"
            + "    input = input.cloneNode(false);\n"
            + "    alert(input.value + '-' + input.defaultValue + '-' + input.getAttribute('value'));\n"

            + "    var builder = document.createElement('div');\n"
            + "    builder.innerHTML = '<input type=\"file\">';\n"
            + "    input = builder.firstChild;\n"
            + "    input = input.cloneNode(false);\n"
            + "    alert(input.value + '-' + input.defaultValue + '-' + input.getAttribute('value'));\n"
            + "  }\n"
            + "</script>\n"
            + "</head><body onload='test()'>\n"
            + "<form>\n"
            + "  <input type='file' id='file1'>\n"
            + "</form>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"-initial-initial", "-initial-initial",
                    "exception", "-initial-initial", "-initial-initial",
                    "-newDefault-newDefault", "-newDefault-newDefault"},
            IE = {"-initial-initial", "-initial-initial",
                    "-initial-initial", "-initial-initial",
                    "-newDefault-newDefault", "-newDefault-newDefault"})
    public void resetByClick() throws Exception {
        final String html = "<html><head><title>foo</title>\n"
            + "<script>\n"
            + "  function test() {\n"
            + "    var file = document.getElementById('testId');\n"
            + "    alert(file.value + '-' + file.defaultValue + '-' + file.getAttribute('value'));\n"

            + "    document.getElementById('testReset').click;\n"
            + "    alert(file.value + '-' + file.defaultValue + '-' + file.getAttribute('value'));\n"

            + "    try{\n"
            + "      file.value = 'newValue';\n"
            + "    } catch(e) { alert('exception'); }\n"
            + "    alert(file.value + '-' + file.defaultValue + '-' + file.getAttribute('value'));\n"

            + "    document.getElementById('testReset').click;\n"
            + "    alert(file.value + '-' + file.defaultValue + '-' + file.getAttribute('value'));\n"

            + "    file.defaultValue = 'newDefault';\n"
            + "    alert(file.value + '-' + file.defaultValue + '-' + file.getAttribute('value'));\n"

            + "    document.forms[0].reset;\n"
            + "    alert(file.value + '-' + file.defaultValue + '-' + file.getAttribute('value'));\n"
            + "  }\n"
            + "</script>\n"
            + "</head><body onload='test()'>\n"
            + "<form>\n"
            + "  <input type='file' id='testId' value='initial'>\n"
            + "  <input type='reset' id='testReset'>\n"
            + "</form>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"-initial-initial", "-initial-initial",
                "exception", "-initial-initial", "-initial-initial",
                "-newDefault-newDefault", "-newDefault-newDefault"},
            IE = {"-initial-initial", "-initial-initial",
                "-initial-initial", "-initial-initial",
                "-newDefault-newDefault", "-newDefault-newDefault"})
    public void resetByJS() throws Exception {
        final String html = "<html><head><title>foo</title>\n"
            + "<script>\n"
            + "  function test() {\n"
            + "    var file = document.getElementById('testId');\n"
            + "    alert(file.value + '-' + file.defaultValue + '-' + file.getAttribute('value'));\n"

            + "    document.forms[0].reset;\n"
            + "    alert(file.value + '-' + file.defaultValue + '-' + file.getAttribute('value'));\n"

            + "    try{\n"
            + "      file.value = 'newValue';\n"
            + "    } catch(e) { alert('exception'); }\n"
            + "    alert(file.value + '-' + file.defaultValue + '-' + file.getAttribute('value'));\n"

            + "    document.forms[0].reset;\n"
            + "    alert(file.value + '-' + file.defaultValue + '-' + file.getAttribute('value'));\n"

            + "    file.defaultValue = 'newDefault';\n"
            + "    alert(file.value + '-' + file.defaultValue + '-' + file.getAttribute('value'));\n"

            + "    document.forms[0].reset;\n"
            + "    alert(file.value + '-' + file.defaultValue + '-' + file.getAttribute('value'));\n"
            + "  }\n"
            + "</script>\n"
            + "</head><body onload='test()'>\n"
            + "<form>\n"
            + "  <input type='file' id='testId' value='initial'>\n"
            + "</form>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"-initial-initial", "-default-default",
                        "exception", "-default-default", "-attribValue-attribValue",
                        "-newDefault-newDefault"},
            IE = {"-initial-initial", "-default-default",
                    "-default-default", "-attribValue-attribValue",
                    "-newDefault-newDefault"})
    public void value() throws Exception {
        final String html = "<html><head><title>foo</title>\n"
            + "<script>\n"
            + "  function test() {\n"
            + "    var file = document.getElementById('testId');\n"
            + "    alert(file.value + '-' + file.defaultValue + '-' + file.getAttribute('value'));\n"

            + "    file.defaultValue = 'default';\n"
            + "    alert(file.value + '-' + file.defaultValue + '-' + file.getAttribute('value'));\n"

            + "    try{\n"
            + "      file.value = 'newValue';\n"
            + "    } catch(e) { alert('exception'); }\n"
            + "    alert(file.value + '-' + file.defaultValue + '-' + file.getAttribute('value'));\n"

            + "    file.setAttribute('value', 'attribValue');\n"
            + "    alert(file.value + '-' + file.defaultValue + '-' + file.getAttribute('value'));\n"

            + "    file.defaultValue = 'newDefault';\n"
            + "    alert(file.value + '-' + file.defaultValue + '-' + file.getAttribute('value'));\n"
            + "  }\n"
            + "</script>\n"
            + "</head><body onload='test()'>\n"
            + "<form>\n"
            + "  <input type='file' id='testId' value='initial'>\n"
            + "</form>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("textLength not available")
    public void textLength() throws Exception {
        final String html = "<html><head><title>foo</title>\n"
            + "<script>\n"
            + "  function test() {\n"
            + "    var text = document.getElementById('testId');\n"
            + "    if(text.textLength) {\n"
            + "      alert(text.textLength);\n"
            + "    } else {\n"
            + "      alert('textLength not available');\n"
            + "    }\n"
            + "  }\n"
            + "</script>\n"
            + "</head><body onload='test()'>\n"
            + "<form>\n"
            + "  <input type='file' id='testId' value='initial'>\n"
            + "</form>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = {"null", "null", "0"},
            IE = {"ex start", "ex end", "exception"})
    public void selection() throws Exception {
        final String html =
              "<html><head><script>\n"
            + "  function test() {\n"
            + "    var s = getSelection(document.getElementById('text1'));\n"
            + "    if (s != undefined) {\n"
            + "      alert(s.length);\n"
            + "    }\n"
            + "  }\n"
            + "  function getSelection(element) {\n"
            + "    try {\n"
            + "      alert(element.selectionStart);\n"
            + "    } catch(e) { alert('ex start'); }\n"
            + "    try {\n"
            + "      alert(element.selectionEnd);\n"
            + "    } catch(e) { alert('ex end'); }\n"
            + "    try {\n"
            + "      return element.value.substring(element.selectionStart, element.selectionEnd);\n"
            + "    } catch(e) { alert('exception'); }\n"
            + "  }\n"
            + "</script></head>\n"
            + "<body onload='test()'>\n"
            + "  <input type='file' id='text1'/>\n"
            + "</body></html>";
        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if test fails
     */
    @Test
    @Alerts(DEFAULT = {"null,null", "exception value", "null,null",
                        "exception", "null,null",
                        "exception", "null,null"},
            IE = {"exception", "exception",
                        "exception", "exception",
                        "exception", "exception"})
    public void selection2_1() throws Exception {
        selection2(3, 10);
    }

    /**
     * @throws Exception if test fails
     */
    @Test
    @Alerts(DEFAULT = {"null,null", "exception value", "null,null",
                        "exception", "null,null",
                        "exception", "null,null"},
            IE = {"exception", "exception",
                        "exception", "exception",
                        "exception", "exception"})
    public void selection2_2() throws Exception {
        selection2(-3, 15);
    }

    /**
     * @throws Exception if test fails
     */
    @Test
    @Alerts(DEFAULT = {"null,null", "exception value", "null,null",
                        "exception", "null,null",
                        "exception", "null,null"},
            IE = {"exception", "exception",
                        "exception", "exception",
                        "exception", "exception"})
    public void selection2_3() throws Exception {
        selection2(10, 5);
    }

    private void selection2(final int selectionStart, final int selectionEnd) throws Exception {
        final String html = "<html>\n"
            + "<body>\n"
            + "<input id='myTextInput' value='Bonjour' type='file'>\n"
            + "<script>\n"
            + "  var input = document.getElementById('myTextInput');\n"

            + "  try {\n"
            + "    alert(input.selectionStart + ',' + input.selectionEnd);\n"
            + "  } catch(e) { alert('exception'); }\n"

            + "  try{\n"
            + "    input.value = '12345678900';\n"
            + "  } catch(e) { alert('exception value'); }\n"
            + "  try {\n"
            + "    alert(input.selectionStart + ',' + input.selectionEnd);\n"
            + "  } catch(e) { alert('exception'); }\n"

            + "  try {\n"
            + "    input.selectionStart = " + selectionStart + ";\n"
            + "  } catch(e) { alert('exception'); }\n"
            + "  try {\n"
            + "    alert(input.selectionStart + ',' + input.selectionEnd);\n"
            + "  } catch(e) { alert('exception'); }\n"

            + "  try {\n"
            + "    input.selectionEnd = " + selectionEnd + ";\n"
            + "  } catch(e) { alert('exception'); }\n"
            + "  try {\n"
            + "    alert(input.selectionStart + ',' + input.selectionEnd);\n"
            + "  } catch(e) { alert('exception'); }\n"
            + "</script>\n"
            + "</body>\n"
            + "</html>";
        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if test fails
     */
    @Test
    @Alerts(DEFAULT = {"null,null", "exception"},
            IE = "exception")
    public void selectionOnUpdate() throws Exception {
        final String html = "<html>\n"
            + "<body>\n"
            + "<input id='myTextInput' value='Hello' type='file'>\n"
            + "<script>\n"
            + "  var input = document.getElementById('myTextInput');\n"

            + "  try {\n"
            + "    alert(input.selectionStart + ',' + input.selectionEnd);\n"

            + "    input.selectionStart = 4;\n"
            + "    input.selectionEnd = 5;\n"
            + "    alert(input.selectionStart + ',' + input.selectionEnd);\n"
            + "    input.value = 'abcdefghif';\n"
            + "    alert(input.selectionStart + ',' + input.selectionEnd);\n"

            + "    input.value = 'abcd';\n"
            + "    alert(input.selectionStart + ',' + input.selectionEnd);\n"

            + "    input.selectionStart = 0;\n"
            + "    input.selectionEnd = 4;\n"

            + "    input.value = 'a';\n"
            + "    alert(input.selectionStart + ',' + input.selectionEnd);\n"
            + "  } catch(e) { alert('exception'); }\n"
            + "</script>\n"
            + "</body>\n"
            + "</html>";
        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"changed2", "changed"})
    public void firingOnchange() throws Exception {
        final String html = "<html><body>\n"
            + "<form onchange='alert(\"changed\")'>\n"
            + "  <input type='file' id='file1' onchange='alert(\"changed2\")' "
                + "onkeydown='alert(\"onkeydown2\")' "
                + "onkeypress='alert(\"onkeypress2\")' "
                + "onkeyup='alert(\"onkeyup2\")'>\n"
            + "</form>\n"
            + "</body></html>";

        final WebDriver driver = loadPage2(html);
        final File tmpFile = File.createTempFile("htmlunit-test", ".txt");
        driver.findElement(By.id("file1")).sendKeys(tmpFile.getAbsolutePath());
        assertTrue(tmpFile.delete());

        verifyAlerts(driver, getExpectedAlerts());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"true", "true"})
    public void nonZeroWidthHeight() throws Exception {
        final String html = "<html><head><title>foo</title>\n"
                + "<script>\n"
                + "  function test() {\n"
                + "    var file = document.getElementById('testId');\n"
                + "    alert(file.clientWidth > 2);\n"
                + "    alert(file.clientHeight > 2);\n"
                + "  }\n"
                + "</script>\n"
                + "</head><body onload='test()'>\n"
                + "<form>\n"
                + "  <input type='file' id='testId'>\n"
                + "</form>\n"
                + "</body></html>";
        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("C:\\fakepath\\pom.xml--null")
    // since 2.28
    // there is an option for IE, for local and trusted sites IE includes the file path
    // because we do not support any IE specific setting we do not send the filename as
    // done by the other browsers
    public void value2() throws Exception {
        final String html =
              "<html>\n"
              + "<head>\n"
              + "<script>\n"
              + "  function test() {\n"
              + "    var input = document.getElementById('f');\n"
              + "    alert(input.value + '-' + input.defaultValue + '-' + input.getAttribute('value'));\n"
              + "  }\n"
              + "</script>\n"
              + "</head>\n"
              + "<body>\n"
              + "  <input type='file' id='f'>\n"
              + "  <button id='clickMe' onclick='test()'>Click Me</button>\n"
              + "</body></html>";

        final String absolutePath = new File("pom.xml").getAbsolutePath();

        final WebDriver driver = loadPage2(html);
        driver.findElement(By.id("f")).sendKeys(absolutePath);
        driver.findElement(By.id("clickMe")).click();

        assertEquals(getExpectedAlerts(), getCollectedAlerts(driver));
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"--null", "--"})
    public void setAttribute() throws Exception {
        final String html =
              "<html>\n"
              + "<head>\n"
              + "<script>\n"
              + "  function test() {\n"
              + "    var input = document.createElement('input');\n"
              + "    alert(input.value + '-' + input.defaultValue + '-' + input.getAttribute('value'));\n"
              + "    input.setAttribute('value', '');\n"
              + "    alert(input.value + '-' + input.defaultValue + '-' + input.getAttribute('value'));\n"
              + "  }\n"
              + "</script>\n"
              + "</head>\n"
              + "<body onload='test()'>\n"
              + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("foo, change")
    public void onchange() throws Exception {
        final String html =
              "<html>\n"
              + "<head>\n"
              + "</head>\n"
              + "<body>\n"
              + "  <input type='file' id='f' value='Hello world'"
              + "      onChange='alert(\"foo\");alert(event.type);'>\n"
              + "  <button id='clickMe' onclick='test()'>Click Me</button>\n"
              + "</body></html>";

        final String absolutePath = new File("pom.xml").getAbsolutePath();

        final WebDriver driver = loadPage2(html);
        driver.findElement(By.id("f")).sendKeys(absolutePath);
        assertEquals(getExpectedAlerts(), getCollectedAlerts(driver, 2));
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("C:\\fakepath\\pom.xml")
    // since 2.28
    // there is an option for IE, for local and trusted sites IE includes the file path
    // because we do not support any IE specific setting we do not send the filename as
    // done by the other browsers
    public void getAttribute() throws Exception {
        final String html =
              "<html><body>\n"
              + "  <input type='file' id='f'>\n"
              + "</body></html>";

        final String absolutePath = new File("pom.xml").getAbsolutePath();

        final WebDriver driver = loadPage2(html);
        final WebElement e = driver.findElement(By.id("f"));
        e.sendKeys(absolutePath);
        assertEquals(getExpectedAlerts()[0], e.getAttribute("value"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("--")
    public void minMaxStep() throws Exception {
        final String html = "<html>\n"
            + "<head>\n"
            + "<script>\n"
            + "  function test() {\n"
            + "    var input = document.getElementById('tester');\n"
            + "    alert(input.min + '-' + input.max + '-' + input.step);\n"
            + "  }\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "<form>\n"
            + "  <input type='file' id='tester'>\n"
            + "</form>\n"
            + "</body>\n"
            + "</html>";

        loadPageWithAlerts2(html);
    }
}
