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
package com.gargoylesoftware.htmlunit.attachment;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.HttpWebConnectionTest;
import com.gargoylesoftware.htmlunit.MockWebConnection;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.SimpleWebTestCase;
import com.gargoylesoftware.htmlunit.TextPage;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebResponse;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.util.MimeType;
import com.gargoylesoftware.htmlunit.util.NameValuePair;

/**
 * Tests for {@link Attachment}.
 *
 * @author Bruce Chapman
 * @author Sudhan Moghe
 * @author Daniel Gredler
 * @author Ronald Brill
 */
@RunWith(BrowserRunner.class)
public class AttachmentTest extends SimpleWebTestCase {

    /**
     * Tests attachment callbacks and the contents of attachments.
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
        assertTrue(HtmlPage.class.isInstance(attachments.get(0).getPage()));
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
        final String content1 = "<html><body>\n"
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
        final String content = "<html>But is it really?</html>";

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
     * This was causing a ClassCastException in Location.setHref as of 2013-10-08 because the TextPage
     * used for the attachment was wrongly associated to the HTMLDocument of the first page.
     * @throws Exception if an error occurs
     */
    @Test
    public void jsChangeLocationAfterReceptionOfAttachment() throws Exception {
        final String html = "<html><body>\n"
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

        final HtmlElement htmlElement = (HtmlElement) page.getFirstByXPath("//input");
        htmlElement.click(); // exception was occurring here
    }

    /**
     * Tests attachment callbacks and the contents of attachments.
     * @throws Exception if an error occurs
     */
    @Test
    public void handleResponseFromHanlder() throws Exception {
        final String content1 = "<html><body>\n"
            + "<form method='POST' name='form' action='" + URL_SECOND + "'>\n"
            + "<input type='submit' value='ok'>\n"
            + "</form>\n"
            + "<a href='#' onclick='document.form.submit()'>click me</a>\n"
            + "</body></html>";
        final String content2 = "download file contents";

        final WebClient client = getWebClient();
        final List<WebResponse> attachments = new ArrayList<>();

        client.setAttachmentHandler(new AttachmentHandler() {
            @Override
            public boolean handleAttachment(final WebResponse response) {
                attachments.add(response);
                return true;
            }

            @Override
            public void handleAttachment(final Page page) {
                throw new IllegalAccessError("handleAttachment(Page) called");
            }
        });

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
        assertEquals(1, client.getWebWindows().size());

        final WebResponse attachmentResponse = attachments.get(0);
        final InputStream attachmentStream = attachmentResponse.getContentAsStream();
        HttpWebConnectionTest.assertEquals(new ByteArrayInputStream(content2.getBytes()), attachmentStream);
        assertEquals(MimeType.TEXT_HTML, attachmentResponse.getContentType());
        assertEquals(200, attachmentResponse.getStatusCode());
        assertEquals(URL_SECOND, attachmentResponse.getWebRequest().getUrl());
    }
}
