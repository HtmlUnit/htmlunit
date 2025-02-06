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
package org.htmlunit.attachment;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.htmlunit.MockWebConnection;
import org.htmlunit.Page;
import org.htmlunit.SimpleWebTestCase;
import org.htmlunit.WebClient;
import org.htmlunit.html.HtmlAnchor;
import org.htmlunit.html.HtmlPage;
import org.htmlunit.junit.BrowserRunner;
import org.htmlunit.util.MimeType;
import org.htmlunit.util.NameValuePair;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Tests for {@link DownloadingAttachmentHandler}.
 *
 * @author Marek Andreansky
 * @author Ronald Brill
 */
@RunWith(BrowserRunner.class)
public class DownloadingAttachmentHandlerTest extends SimpleWebTestCase {

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void basic() throws Exception {
        final String content1 = "<html><body>\n"
            + "<form method='POST' name='form' action='" + URL_SECOND + "'>\n"
            + "<input type='submit' value='ok'>\n"
            + "</form>\n"
            + "<a href='#' onclick='document.form.submit()'>click me</a>\n"
            + "</body></html>";
        final String content2 = "download file contents";

        final File tmpFolder = new File(System.getProperty("java.io.tmpdir"));
        final File downloadFolder = new File(tmpFolder, "downloading-attachment-test");

        try {
            FileUtils.forceMkdir(downloadFolder);

            assertEquals(0, FileUtils.listFiles(downloadFolder, null, false).size());

            final WebClient client = getWebClient();
            client.setAttachmentHandler(new DownloadingAttachmentHandler(downloadFolder.toPath()));

            final List<NameValuePair> headers = new ArrayList<>();
            headers.add(new NameValuePair("Content-Disposition", "attachment"));

            final MockWebConnection conn = new MockWebConnection();
            conn.setResponse(URL_FIRST, content1);
            conn.setResponse(URL_SECOND, content2, 200, "OK", MimeType.TEXT_HTML, headers);
            client.setWebConnection(conn);

            final HtmlPage result = client.getPage(URL_FIRST);
            final HtmlAnchor anchor = result.getAnchors().get(0);
            final Page clickResult = anchor.click();
            assertEquals(result, clickResult);

            Collection<File> files = FileUtils.listFiles(downloadFolder, null, false);
            assertEquals(1, files.size());
            assertEquals("download", files.iterator().next().getName());

            anchor.click();
            files = FileUtils.listFiles(downloadFolder, null, false);
            assertEquals(2, files.size());

            // be order agnostic
            assertTrue(files.removeIf(f -> f.getName().equals("download")));
            assertTrue(files.removeIf(f -> f.getName().equals("download(1)")));
            assertEquals(0, files.size());
        }
        finally {
            FileUtils.deleteDirectory(downloadFolder);
        }
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void basicFileNameFromUrl() throws Exception {
        final String content1 = "<html><body>\n"
            + "<form method='POST' name='form' action='" + URL_SECOND + "test.txt'>\n"
            + "<input type='submit' value='ok'>\n"
            + "</form>\n"
            + "<a href='#' onclick='document.form.submit()'>click me</a>\n"
            + "</body></html>";
        final String content2 = "download file contents";

        final File tmpFolder = new File(System.getProperty("java.io.tmpdir"));
        final File downloadFolder = new File(tmpFolder, "downloading-attachment-test");

        try {
            FileUtils.forceMkdir(downloadFolder);

            assertEquals(0, FileUtils.listFiles(downloadFolder, null, false).size());

            final WebClient client = getWebClient();
            client.setAttachmentHandler(new DownloadingAttachmentHandler(downloadFolder.toPath()));

            final List<NameValuePair> headers = new ArrayList<>();
            headers.add(new NameValuePair("Content-Disposition", "attachment"));

            final MockWebConnection conn = new MockWebConnection();
            conn.setResponse(URL_FIRST, content1);
            conn.setResponse(new URL(URL_SECOND, "test.txt"), content2, 200, "OK", MimeType.TEXT_HTML, headers);
            client.setWebConnection(conn);

            final HtmlPage result = client.getPage(URL_FIRST);
            final HtmlAnchor anchor = result.getAnchors().get(0);
            final Page clickResult = anchor.click();
            assertEquals(result, clickResult);

            Collection<File> files = FileUtils.listFiles(downloadFolder, null, false);
            assertEquals(1, files.size());
            assertEquals("test.txt", files.iterator().next().getName());

            anchor.click();
            files = FileUtils.listFiles(downloadFolder, null, false);
            assertEquals(2, files.size());

            // be order agnostic
            assertTrue(files.removeIf(f -> f.getName().equals("test.txt")));
            assertTrue(files.removeIf(f -> f.getName().equals("test(1).txt")));
            assertEquals(0, files.size());
        }
        finally {
            FileUtils.deleteDirectory(downloadFolder);
        }
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void basicFileNameFromHeader() throws Exception {
        final String content1 = "<html><body>\n"
            + "<form method='POST' name='form' action='" + URL_SECOND + "test.txt'>\n"
            + "<input type='submit' value='ok'>\n"
            + "</form>\n"
            + "<a href='#' onclick='document.form.submit()'>click me</a>\n"
            + "</body></html>";
        final String content2 = "download file contents";

        final File tmpFolder = new File(System.getProperty("java.io.tmpdir"));
        final File downloadFolder = new File(tmpFolder, "downloading-attachment-test");
        FileUtils.deleteDirectory(downloadFolder);

        try {
            FileUtils.forceMkdir(downloadFolder);

            assertEquals(0, FileUtils.listFiles(downloadFolder, null, false).size());

            final WebClient client = getWebClient();
            client.setAttachmentHandler(new DownloadingAttachmentHandler(downloadFolder.toPath()));

            final List<NameValuePair> headers = new ArrayList<>();
            headers.add(new NameValuePair("Content-Disposition", "attachment; filename=sample.pdf"));

            final MockWebConnection conn = new MockWebConnection();
            conn.setResponse(URL_FIRST, content1);
            conn.setResponse(new URL(URL_SECOND, "test.txt"), content2, 200, "OK", MimeType.TEXT_HTML, headers);
            client.setWebConnection(conn);

            final HtmlPage result = client.getPage(URL_FIRST);
            final HtmlAnchor anchor = result.getAnchors().get(0);
            final Page clickResult = anchor.click();
            assertEquals(result, clickResult);

            Collection<File> files = FileUtils.listFiles(downloadFolder, null, false);
            assertEquals(1, files.size());
            assertEquals("sample.pdf", files.iterator().next().getName());

            anchor.click();
            files = FileUtils.listFiles(downloadFolder, null, false);
            assertEquals(2, files.size());

            // be order agnostic
            assertTrue(files.removeIf(f -> f.getName().equals("sample.pdf")));
            assertTrue(files.removeIf(f -> f.getName().equals("sample(1).pdf")));
            assertEquals(0, files.size());
        }
        finally {
            FileUtils.deleteDirectory(downloadFolder);
        }
    }
}
