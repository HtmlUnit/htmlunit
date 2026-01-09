/*
 * Copyright (c) 2002-2026 Gargoyle Software Inc.
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

import java.io.File;
import java.net.URL;
import java.net.UnknownHostException;
import java.nio.file.Path;

import org.apache.commons.io.FileUtils;
import org.htmlunit.MockWebConnection;
import org.htmlunit.SimpleWebTestCase;
import org.htmlunit.WebClient;
import org.htmlunit.util.MimeType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

/**
 * Tests for {@link XmlSerializer}.
 *
 * @author Ahmed Ashour
 * @author Ronald Brill
 */
public class XmlSerializerTest extends SimpleWebTestCase {

    @TempDir
    static Path TEMP_DIR_;

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void notExistingLink() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html>\n"
                + "<body>"
                + "  <link rel='alternate' href='none.jpg'>\n"
                + "</body>\n"
                + "</html>";

        final WebClient client = getWebClient();
        final MockWebConnection connection = getMockWebConnection();
        connection.setDefaultResponse("Error: not found", 404, "Not Found", MimeType.TEXT_HTML);
        connection.setResponse(URL_FIRST, html);
        client.setWebConnection(connection);

        final HtmlPage page = client.getPage(URL_FIRST);

        final File tmpFolder = new File(TEMP_DIR_.toFile(), "hu");
        tmpFolder.mkdir();
        final File file = new File(tmpFolder, "hu_XmlSerializerTest_notExistingLink.html");
        FileUtils.deleteQuietly(file);

        page.save(file);
        assertTrue(file.exists());
        assertTrue(file.isFile());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void unknownHostExceptionLink() throws Exception {
        final URL imageUrl = new URL(URL_FIRST, "none.jpg");
        final String html = DOCTYPE_HTML
                + "<html>\n"
                + "<body>"
                + "  <link rel='alternate' href='" + imageUrl.toExternalForm() + "'>\n"
                + "</body>\n"
                + "</html>";

        final WebClient client = getWebClient();
        final MockWebConnection connection = getMockWebConnection();
        connection.setThrowable(imageUrl, new UnknownHostException(imageUrl.toExternalForm()));
        connection.setResponse(URL_FIRST, html);
        client.setWebConnection(connection);

        final HtmlPage page = client.getPage(URL_FIRST);

        final File tmpFolder = new File(TEMP_DIR_.toFile(), "hu");
        tmpFolder.mkdir();
        final File file = new File(tmpFolder, "hu_XmlSerializerTest_unknownHostExceptionLink.html");
        FileUtils.deleteQuietly(file);

        page.save(file);
        assertTrue(file.exists());
        assertTrue(file.isFile());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void unsupportedProtocolLink() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html><head>"
                + "<link rel='alternate' href='android-app://somehost'>\n"
                + "</head></html>";

        final WebClient client = getWebClient();
        final MockWebConnection connection = getMockWebConnection();
        connection.setResponse(URL_FIRST, html);
        client.setWebConnection(connection);

        final HtmlPage page = client.getPage(URL_FIRST);

        final File tmpFolder = new File(TEMP_DIR_.toFile(), "hu");
        tmpFolder.mkdir();
        final File file = new File(tmpFolder, "hu_XmlSerializerTest_unsupportedProtocol.html");
        FileUtils.deleteQuietly(file);

        page.save(file);
        assertTrue(file.exists());
        assertTrue(file.isFile());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void notExistingImage() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html>\n"
                + "<body>"
                + "  <img src='none.jpg'>\n"
                + "</body>\n"
                + "</html>";

        final WebClient client = getWebClient();
        final MockWebConnection connection = getMockWebConnection();
        connection.setDefaultResponse("Error: not found", 404, "Not Found", MimeType.TEXT_HTML);
        connection.setResponse(URL_FIRST, html);
        client.setWebConnection(connection);

        final HtmlPage page = client.getPage(URL_FIRST);

        final File tmpFolder = new File(TEMP_DIR_.toFile(), "hu");
        tmpFolder.mkdir();
        final File file = new File(tmpFolder, "hu_XmlSerializerTest_notExistingImage.html");
        FileUtils.deleteQuietly(file);

        page.save(file);
        assertTrue(file.exists());
        assertTrue(file.isFile());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void unknownHostExceptionImage() throws Exception {
        final URL imageUrl = new URL(URL_FIRST, "none.jpg");
        final String html = DOCTYPE_HTML
                + "<html>\n"
                + "<body>"
                + "  <img src='" + imageUrl.toExternalForm() + "'>\n"
                + "</body>\n"
                + "</html>";

        final WebClient client = getWebClient();
        final MockWebConnection connection = getMockWebConnection();
        connection.setThrowable(imageUrl, new UnknownHostException(imageUrl.toExternalForm()));
        connection.setResponse(URL_FIRST, html);
        client.setWebConnection(connection);

        final HtmlPage page = client.getPage(URL_FIRST);

        final File tmpFolder = new File(TEMP_DIR_.toFile(), "hu");
        tmpFolder.mkdir();
        final File file = new File(tmpFolder, "hu_XmlSerializerTest_unknownHostExceptionImage.html");
        FileUtils.deleteQuietly(file);

        page.save(file);
        assertTrue(file.exists());
        assertTrue(file.isFile());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void unsupportedProtocolImage() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html>\n"
                + "<body>"
                + "  <img src='android-app://somehost'>\n"
                + "</body>\n"
                + "</html>";

        final WebClient client = getWebClient();
        final MockWebConnection connection = getMockWebConnection();
        connection.setResponse(URL_FIRST, html);
        client.setWebConnection(connection);

        final HtmlPage page = client.getPage(URL_FIRST);

        final File tmpFolder = new File(TEMP_DIR_.toFile(), "hu");
        tmpFolder.mkdir();
        final File file = new File(tmpFolder, "hu_XmlSerializerTest_unsupportedProtocolImage.html");
        FileUtils.deleteQuietly(file);

        page.save(file);
        assertTrue(file.exists());
        assertTrue(file.isFile());
    }
}
