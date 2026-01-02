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

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.htmlunit.HttpWebConnectionTest;
import org.htmlunit.MockWebConnection;
import org.htmlunit.Page;
import org.htmlunit.SimpleWebTestCase;
import org.htmlunit.TextPage;
import org.htmlunit.WebClient;
import org.htmlunit.WebResponse;
import org.htmlunit.html.HtmlAnchor;
import org.htmlunit.html.HtmlElement;
import org.htmlunit.html.HtmlPage;
import org.htmlunit.util.MimeType;
import org.htmlunit.util.NameValuePair;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link Attachment}.
 *
 * @author Bruce Chapman
 * @author Sudhan Moghe
 * @author Daniel Gredler
 * @author Ronald Brill
 * @author Lai Quang Duong
 */
public class AttachmentTest extends SimpleWebTestCase {

    /**
     * Tests attachment callbacks and the contents of attachments.
     * @throws Exception if an error occurs
     */
    @Test
    public void basic() throws Exception {
        final String content1 = DOCTYPE_HTML
            + "<html><body>\n"
            + "<form method='POST' name='form' action='" + URL_SECOND + "'>\n"
            + "<input type='submit' value='ok'>\n"
            + "</form>\n"
            + "<a href='#' onclick='document.form.submit()'>click me</a>\n"
            + "</body></html>";
        final String content2 = "download file contents";

        final WebClient client = getWebClient();
        final List<Attachment> attachments = new ArrayList<>();
        client.setAttachmentHandler(new CollectingAttachmentHandler(attachments));

        final List<NameValuePair> headers = new ArrayList<>();
        headers.add(new NameValuePair("Content-Disposition", "attachment"));

        final MockWebConnection conn = new MockWebConnection();
        conn.setResponse(URL_FIRST, content1);
        conn.setResponse(URL_SECOND, content2, 200, "OK", MimeType.TEXT_HTML, headers);
        client.setWebConnection(conn);
        assertTrue(attachments.isEmpty());

        final HtmlPage result = client.getPage(URL_FIRST);
        final HtmlAnchor anchor = result.getAnchors().get(0);
        final Page clickResult = anchor.click();
        assertEquals(result, clickResult);
        assertEquals(1, attachments.size());
        assertTrue(attachments.get(0).getPage() instanceof HtmlPage);
        // the attachment is opened inside a new window
        assertEquals(2, client.getWebWindows().size());

        final Attachment attachment = attachments.get(0);
        final Page attachedPage = attachment.getPage();
        final WebResponse attachmentResponse = attachedPage.getWebResponse();
        final InputStream attachmentStream = attachmentResponse.getContentAsStream();
        HttpWebConnectionTest.assertEquals(new ByteArrayInputStream(content2.getBytes()), attachmentStream);
        assertEquals(MimeType.TEXT_HTML, attachmentResponse.getContentType());
        assertEquals(200, attachmentResponse.getStatusCode());
        assertEquals(URL_SECOND, attachmentResponse.getWebRequest().getUrl());
    }

    /**
     * Tests attachment callbacks and the contents of attachments.
     * @throws Exception if an error occurs
     */
    @Test
    public void contentDispositionCaseInsensitive() throws Exception {
        final String content1 = DOCTYPE_HTML
            + "<html><body>\n"
            + "<form method='POST' name='form' action='" + URL_SECOND + "'>\n"
            + "<input type='submit' value='ok'>\n"
            + "</form>\n"
            + "<a href='#' onclick='document.form.submit()'>click me</a>\n"
            + "</body></html>";
        final String content2 = "download file contents";

        final WebClient client = getWebClient();
        final List<Attachment> attachments = new ArrayList<>();
        client.setAttachmentHandler(new CollectingAttachmentHandler(attachments));

        final List<NameValuePair> headers = new ArrayList<>();
        headers.add(new NameValuePair("Content-Disposition", "AttachMent"));

        final MockWebConnection conn = new MockWebConnection();
        conn.setResponse(URL_FIRST, content1);
        conn.setResponse(URL_SECOND, content2, 200, "OK", MimeType.TEXT_HTML, headers);
        client.setWebConnection(conn);
        assertTrue(attachments.isEmpty());

        final HtmlPage result = client.getPage(URL_FIRST);
        final HtmlAnchor anchor = result.getAnchors().get(0);
        final Page clickResult = anchor.click();
        assertEquals(result, clickResult);
        assertEquals(1, attachments.size());
    }

    /**
     * Tests {@link Attachment#getSuggestedFilename()}.
     * @throws Exception if an error occurs
     */
    @Test
    public void filename() throws Exception {
        final String content = DOCTYPE_HTML + "<html>But is it really?</html>";

        final WebClient client = getWebClient();
        final MockWebConnection conn = new MockWebConnection();
        client.setWebConnection(conn);
        final List<Attachment> attachments = new ArrayList<>();
        client.setAttachmentHandler(new CollectingAttachmentHandler(attachments));

        final List<NameValuePair> headers1 = new ArrayList<>();
        headers1.add(new NameValuePair("Content-Disposition", "attachment;filename=\"hello.html\""));
        conn.setResponse(URL_FIRST, content, 200, "OK", MimeType.TEXT_HTML, headers1);
        client.getPage(URL_FIRST);
        final Attachment result = attachments.get(0);
        assertEquals(result.getSuggestedFilename(), "hello.html");
        attachments.clear();

        final List<NameValuePair> headers2 = new ArrayList<>();
        headers2.add(new NameValuePair("Content-Disposition", "attachment; filename=hello2.html; something=else"));
        conn.setResponse(URL_SECOND, content, 200, "OK", MimeType.TEXT_PLAIN, headers2);
        client.getPage(URL_SECOND);
        final Attachment result2 = attachments.get(0);
        assertEquals(result2.getSuggestedFilename(), "hello2.html");
        assertEquals(content, ((TextPage) result2.getPage()).getContent());
        attachments.clear();

        final List<NameValuePair> headers3 = new ArrayList<>();
        headers3.add(new NameValuePair("Content-Disposition", "attachment; filename="));
        conn.setResponse(URL_SECOND, content, 200, "OK", MimeType.TEXT_PLAIN, headers3);
        client.getPage(URL_SECOND);
        final Attachment result3 = attachments.get(0);
        assertNull(result3.getSuggestedFilename());
        assertEquals(content, ((TextPage) result3.getPage()).getContent());
        attachments.clear();

        final List<NameValuePair> headers4 = new ArrayList<>();
        headers4.add(new NameValuePair("Content-Disposition", "attachment"));
        final byte[] contentb = new byte[] {(byte) 0xCA, (byte) 0xFE, (byte) 0xBA, (byte) 0xBE};
        conn.setResponse(URL_THIRD, contentb, 200, "OK", "application/x-rubbish", headers4);
        client.getPage(URL_THIRD);
        final Attachment result4 = attachments.get(0);
        final InputStream result4Stream = result4.getPage().getWebResponse().getContentAsStream();
        assertNull(result4.getSuggestedFilename());
        assertEquals(result4.getPage().getWebResponse().getContentType(), "application/x-rubbish");
        HttpWebConnectionTest.assertEquals(new ByteArrayInputStream(contentb), result4Stream);
        attachments.clear();
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void filenameFromAnchor() throws Exception {
        final WebClient client = getWebClient();
        final MockWebConnection conn = new MockWebConnection();
        client.setWebConnection(conn);
        final List<Attachment> attachments = new ArrayList<>();
        client.setAttachmentHandler(new CollectingAttachmentHandler(attachments));

        final String content = DOCTYPE_HTML
                + "<html>\n"
                + "<head>\n"
                + "<script>\n"
                + "var blob = new Blob(['foo'], {type: 'text/plain'}),\n"
                + "  url = URL.createObjectURL(blob),\n"
                + "  elem = document.createElement('a');\n"
                + "elem.href = url, elem.download = 'foo.txt', elem.click();\n"
                + "</script>\n"
                + "</head>\n"
                + "</html>\n";

        conn.setResponse(URL_FIRST, content);
        client.getPage(URL_FIRST);

        final Attachment result = attachments.get(0);
        assertEquals(result.getSuggestedFilename(), "foo.txt");
        assertEquals("foo", ((TextPage) result.getPage()).getContent());
        attachments.clear();
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void filenameFromFile() throws Exception {
        final WebClient client = getWebClient();
        final MockWebConnection conn = new MockWebConnection();
        client.setWebConnection(conn);
        final List<Attachment> attachments = new ArrayList<>();
        client.setAttachmentHandler(new CollectingAttachmentHandler(attachments));

        final String content = DOCTYPE_HTML
                + "<html>\n"
                + "<head>\n"
                + "<script>\n"
                + "var blob = new File(['bar'], 'bar.txt', {type: 'text/plain'}),\n"
                + "  url = URL.createObjectURL(blob),\n"
                + "  elem = document.createElement('a');\n"
                + "elem.href = url, elem.download = '', elem.click();\n"
                + "</script>\n"
                + "</head>\n"
                + "</html>\n";

        conn.setResponse(URL_FIRST, content);
        client.getPage(URL_FIRST);

        final Attachment result = attachments.get(0);
        assertEquals(result.getSuggestedFilename(), "bar.txt");
        assertEquals("bar", ((TextPage) result.getPage()).getContent());
        attachments.clear();
    }


    /**
     * Prioritize name from Anchor.download over File.name.
     * @throws Exception if an error occurs
     */
    @Test
    public void filenameFromFilePrioritizeAnchorDownloadOverFileName() throws Exception {
        final WebClient client = getWebClient();
        final MockWebConnection conn = new MockWebConnection();
        client.setWebConnection(conn);
        final List<Attachment> attachments = new ArrayList<>();
        client.setAttachmentHandler(new CollectingAttachmentHandler(attachments));

        final String content = DOCTYPE_HTML
                + "<html>\n"
                + "<head>\n"
                + "<script>\n"
                + "var blob = new File(['bar'], 'bar.txt', {type: 'text/plain'}),\n"
                + "  url = URL.createObjectURL(blob),\n"
                + "  elem = document.createElement('a');\n"
                + "elem.href = url, elem.download = 'foobar.txt', elem.click();\n"
                + "</script>\n"
                + "</head>\n"
                + "</html>\n";

        conn.setResponse(URL_FIRST, content);
        client.getPage(URL_FIRST);

        final Attachment result = attachments.get(0);
        assertEquals(result.getSuggestedFilename(), "foobar.txt");
        assertEquals("bar", ((TextPage) result.getPage()).getContent());
        attachments.clear();
    }

    /**
     * This was causing a ClassCastException in Location.setHref as of 2013-10-08 because the TextPage
     * used for the attachment was wrongly associated to the HTMLDocument of the first page.
     * @throws Exception if an error occurs
     */
    @Test
    public void jsChangeLocationAfterReceptionOfAttachment() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><body>\n"
            + "<form action='action'>\n"
            + "<input type='submit' onclick='window.location=\"foo\"; return false'>\n"
            + "</form>\n"
            + "<a href='" + URL_SECOND + "'>download</a>\n"
            + "</body></html>";

        final WebClient client = getWebClient();
        final List<Attachment> attachments = new ArrayList<>();
        client.setAttachmentHandler(new CollectingAttachmentHandler(attachments));

        final List<NameValuePair> headers = new ArrayList<>();
        headers.add(new NameValuePair("Content-Disposition", "attachment"));

        final MockWebConnection conn = getMockWebConnection();
        conn.setDefaultResponse("");
        conn.setResponse(URL_SECOND, "some text", 200, "OK", MimeType.TEXT_PLAIN, headers);

        final HtmlPage page = loadPage(html);
        // download text attachment
        page.getAnchors().get(0).click();
        assertEquals(1, attachments.size());

        final HtmlElement htmlElement = page.getFirstByXPath("//input");
        htmlElement.click(); // exception was occurring here
    }

    /**
     * Tests attachment callbacks and the contents of attachments.
     * @throws Exception if an error occurs
     */
    @Test
    public void handleResponseFromHandler() throws Exception {
        final String content1 = DOCTYPE_HTML
            + "<html><body>\n"
            + "<form method='POST' name='form' action='" + URL_SECOND + "'>\n"
            + "<input type='submit' value='ok'>\n"
            + "</form>\n"
            + "<a href='#' onclick='document.form.submit()'>click me</a>\n"
            + "</body></html>";
        final String content2 = "download file contents";

        final WebClient client = getWebClient();
        final List<WebResponse> attachments = new ArrayList<>();
        final List<String> attachmentFilenames = new ArrayList<>();

        client.setAttachmentHandler(new AttachmentHandler() {
            @Override
            public boolean handleAttachment(final WebResponse response, final String attachmentFilename) {
                attachments.add(response);
                attachmentFilenames.add(attachmentFilename);
                return true;
            }

            @Override
            public void handleAttachment(final Page page, final String attachmentFilename) {
                throw new IllegalAccessError("handleAttachment(Page, String) called");
            }
        });

        final List<NameValuePair> headers = new ArrayList<>();
        headers.add(new NameValuePair("Content-Disposition", "attachment"));

        final MockWebConnection conn = new MockWebConnection();
        conn.setResponse(URL_FIRST, content1);
        conn.setResponse(URL_SECOND, content2, 200, "OK", MimeType.TEXT_HTML, headers);
        client.setWebConnection(conn);
        assertTrue(attachments.isEmpty());
        assertTrue(attachmentFilenames.isEmpty());

        final HtmlPage result = client.getPage(URL_FIRST);
        final HtmlAnchor anchor = result.getAnchors().get(0);
        final Page clickResult = anchor.click();
        assertEquals(result, clickResult);
        assertEquals(1, attachments.size());
        assertEquals(1, attachmentFilenames.size());
        assertEquals(1, client.getWebWindows().size());

        final WebResponse attachmentResponse = attachments.get(0);
        final InputStream attachmentStream = attachmentResponse.getContentAsStream();
        HttpWebConnectionTest.assertEquals(new ByteArrayInputStream(content2.getBytes()), attachmentStream);
        assertEquals(MimeType.TEXT_HTML, attachmentResponse.getContentType());
        assertEquals(200, attachmentResponse.getStatusCode());
        assertEquals(URL_SECOND, attachmentResponse.getWebRequest().getUrl());

        assertNull(attachmentFilenames.get(0));
    }

    /**
     * Tests attachment callbacks and the contents of attachments.
     * @throws Exception if an error occurs
     */
    @Test
    public void handleResponseFromHandlerWithFileName() throws Exception {
        final String content1 = DOCTYPE_HTML
            + "<html><body>\n"
            + "<form method='POST' name='form' action='" + URL_SECOND + "'>\n"
            + "<input type='submit' value='ok'>\n"
            + "</form>\n"
            + "<a href='#' onclick='document.form.submit()'>click me</a>\n"
            + "</body></html>";
        final String content2 = "download file contents";

        final WebClient client = getWebClient();
        final List<WebResponse> attachments = new ArrayList<>();
        final List<String> attachmentFilenames = new ArrayList<>();

        client.setAttachmentHandler(new AttachmentHandler() {
            @Override
            public boolean handleAttachment(final WebResponse response, final String attachmentFilename) {
                attachments.add(response);
                attachmentFilenames.add(attachmentFilename);
                return true;
            }

            @Override
            public void handleAttachment(final Page page, final String attachmentFilename) {
                throw new IllegalAccessError("handleAttachment(Page, String) called");
            }
        });

        final List<NameValuePair> headers = new ArrayList<>();
        headers.add(new NameValuePair("Content-Disposition", "attachment; filename=htmlunit.zip"));

        final MockWebConnection conn = new MockWebConnection();
        conn.setResponse(URL_FIRST, content1);
        conn.setResponse(URL_SECOND, content2, 200, "OK", MimeType.TEXT_HTML, headers);
        client.setWebConnection(conn);
        assertTrue(attachments.isEmpty());
        assertTrue(attachmentFilenames.isEmpty());

        final HtmlPage result = client.getPage(URL_FIRST);
        final HtmlAnchor anchor = result.getAnchors().get(0);
        final Page clickResult = anchor.click();
        assertEquals(result, clickResult);
        assertEquals(1, attachments.size());
        assertEquals(1, attachmentFilenames.size());
        assertEquals(1, client.getWebWindows().size());

        final WebResponse attachmentResponse = attachments.get(0);
        final InputStream attachmentStream = attachmentResponse.getContentAsStream();
        HttpWebConnectionTest.assertEquals(new ByteArrayInputStream(content2.getBytes()), attachmentStream);
        assertEquals(MimeType.TEXT_HTML, attachmentResponse.getContentType());
        assertEquals(200, attachmentResponse.getStatusCode());
        assertEquals(URL_SECOND, attachmentResponse.getWebRequest().getUrl());

        assertEquals("htmlunit.zip", attachmentFilenames.get(0));
    }

    /**
     * Tests attachment callbacks and the contents of attachments.
     * @throws Exception if an error occurs
     */
    @Test
    public void handleResponseOnlyApplicationOctetstream() throws Exception {
        final String content1 = DOCTYPE_HTML
            + "<html><body>\n"
            + "<form method='POST' name='form' action='" + URL_SECOND + "'>\n"
            + "<input type='submit' value='ok'>\n"
            + "</form>\n"
            + "<a href='#' onclick='document.form.submit()'>click me</a>\n"
            + "</body></html>";
        final String content2 = "download file contents";

        final WebClient client = getWebClient();
        final List<WebResponse> attachments = new ArrayList<>();
        final List<String> attachmentFilenames = new ArrayList<>();

        client.setAttachmentHandler(new AttachmentHandler() {
            @Override
            public boolean handleAttachment(final WebResponse response, final String attachmentFilename) {
                attachments.add(response);
                attachmentFilenames.add(attachmentFilename);
                return true;
            }

            @Override
            public void handleAttachment(final Page page, final String attachmentFilename) {
                throw new IllegalAccessError("handleAttachment(Page, String) called");
            }
        });

        final List<NameValuePair> headers = new ArrayList<>();
        headers.add(new NameValuePair("Content-Type", MimeType.APPLICATION_OCTET_STREAM));

        final MockWebConnection conn = new MockWebConnection();
        conn.setResponse(URL_FIRST, content1);
        conn.setResponse(URL_SECOND, content2, 200, "OK", MimeType.TEXT_HTML, headers);
        client.setWebConnection(conn);
        assertTrue(attachments.isEmpty());
        assertTrue(attachmentFilenames.isEmpty());

        final HtmlPage result = client.getPage(URL_FIRST);
        final HtmlAnchor anchor = result.getAnchors().get(0);
        final Page clickResult = anchor.click();
        assertEquals(result, clickResult);
        assertEquals(1, attachments.size());
        assertEquals(1, attachmentFilenames.size());
        assertEquals(1, client.getWebWindows().size());

        final WebResponse attachmentResponse = attachments.get(0);
        final InputStream attachmentStream = attachmentResponse.getContentAsStream();
        HttpWebConnectionTest.assertEquals(new ByteArrayInputStream(content2.getBytes()), attachmentStream);
        assertEquals(MimeType.APPLICATION_OCTET_STREAM, attachmentResponse.getContentType());
        assertEquals(200, attachmentResponse.getStatusCode());
        assertEquals(URL_SECOND, attachmentResponse.getWebRequest().getUrl());

        assertNull(attachmentFilenames.get(0));
    }
}
