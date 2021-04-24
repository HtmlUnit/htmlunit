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

import java.io.File;
import java.net.URL;
import java.net.UnknownHostException;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.junit.runner.RunWith;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.MockWebConnection;
import com.gargoylesoftware.htmlunit.SimpleWebTestCase;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.util.MimeType;

/**
 * Tests for {@link XmlSerializer}.
 *
 * @author Ahmed Ashour
 * @author Ronald Brill
 */
@RunWith(BrowserRunner.class)
public class XmlSerializerTest extends SimpleWebTestCase {

    /**
     * Utility for temporary folders.
     * Has to be public due to JUnit's constraints for @Rule.
     */
    @Rule
    public final TemporaryFolder tmpFolderProvider_ = new TemporaryFolder();

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void notExistingLink() throws Exception {
        final String html =
                "<html>\n"
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

        final File tmpFolder = tmpFolderProvider_.newFolder("hu");
        final File file = new File(tmpFolder, "hu_XmlSerializerTest_notExistingLink.html");
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
        final String html =
                "<html>\n"
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

        final File tmpFolder = tmpFolderProvider_.newFolder("hu");
        final File file = new File(tmpFolder, "hu_XmlSerializerTest_unknownHostExceptionLink.html");
        page.save(file);
        assertTrue(file.exists());
        assertTrue(file.isFile());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void unsupportedProtocolLink() throws Exception {
        final String html = "<html><head>"
                + "<link rel='alternate' href='android-app://somehost'>\n"
                + "</head></html>";

        final WebClient client = getWebClient();
        final MockWebConnection connection = getMockWebConnection();
        connection.setResponse(URL_FIRST, html);
        client.setWebConnection(connection);

        final HtmlPage page = client.getPage(URL_FIRST);

        final File tmpFolder = tmpFolderProvider_.newFolder("hu");
        final File file = new File(tmpFolder, "hu_XmlSerializerTest_unsupportedProtocol.html");
        page.save(file);
        assertTrue(file.exists());
        assertTrue(file.isFile());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void notExistingImage() throws Exception {
        final String html =
                "<html>\n"
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

        final File tmpFolder = tmpFolderProvider_.newFolder("hu");
        final File file = new File(tmpFolder, "hu_XmlSerializerTest_notExistingImage.html");
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
        final String html =
                "<html>\n"
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

        final File tmpFolder = tmpFolderProvider_.newFolder("hu");
        final File file = new File(tmpFolder, "hu_XmlSerializerTest_unknownHostExceptionImage.html");
        page.save(file);
        assertTrue(file.exists());
        assertTrue(file.isFile());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void unsupportedProtocolImage() throws Exception {
        final String html =
                "<html>\n"
                + "<body>"
                + "  <img src='android-app://somehost'>\n"
                + "</body>\n"
                + "</html>";

        final WebClient client = getWebClient();
        final MockWebConnection connection = getMockWebConnection();
        connection.setResponse(URL_FIRST, html);
        client.setWebConnection(connection);

        final HtmlPage page = client.getPage(URL_FIRST);

        final File tmpFolder = tmpFolderProvider_.newFolder("hu");
        final File file = new File(tmpFolder, "hu_XmlSerializerTest_unsupportedProtocolImage.html");
        page.save(file);
        assertTrue(file.exists());
        assertTrue(file.isFile());
    }
}
