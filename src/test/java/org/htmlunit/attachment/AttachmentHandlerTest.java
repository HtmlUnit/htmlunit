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
package org.htmlunit.attachment;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.htmlunit.HttpMethod;
import org.htmlunit.WebResponse;
import org.htmlunit.WebResponseData;
import org.htmlunit.http.HttpStatus;
import org.htmlunit.util.NameValuePair;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link AttachmentHandler}.
 *
 * @author Ronald Brill
 */
public class AttachmentHandlerTest {

    private static boolean isAttachment(final String contentDisposition, final String contentType)
            throws Exception {
        final List<NameValuePair> headers = new ArrayList<>();
        if (contentDisposition != null) {
            headers.add(new NameValuePair("Content-Disposition", contentDisposition));
        }
        if (contentType != null) {
            headers.add(new NameValuePair("Content-Type", contentType));
        }

        final WebResponseData data = new WebResponseData("HtmlUnit".getBytes(),
                HttpStatus.OK_200, HttpStatus.OK_200_MSG, headers);
        final WebResponse response = new WebResponse(data, new URL("http://test.com"), HttpMethod.GET, 1000);
        final AttachmentHandler attachmentHandler = (page, attachmentFilename) -> {
            // mock
        };

        return attachmentHandler.isAttachment(response);
    }

    /**
     * No headers at all is no attachment.
     * @throws Exception if an error occurs
     */
    @Test
    public void noHeaders() throws Exception {
        final WebResponseData data = new WebResponseData("HtmlUnit".getBytes(),
                HttpStatus.OK_200, HttpStatus.OK_200_MSG, Collections.emptyList());
        final WebResponse response = new WebResponse(data, new URL("http://test.com"), HttpMethod.GET, 1000);
        final AttachmentHandler attachmentHandler = (page, attachmentFilename) -> {
            // mock
        };

        Assertions.assertFalse(attachmentHandler.isAttachment(response));
    }

    /**
     * The type attachment is an attachment.
     * @throws Exception if an error occurs
     */
    @Test
    public void typeAttachment() throws Exception {
        Assertions.assertTrue(isAttachment("attachment", "text/html"));
        Assertions.assertTrue(isAttachment("attachment; filename=\"foo.txt\"", "text/html"));
    }

    /**
     * The disposition type is case-insensitive.
     * @throws Exception if an error occurs
     */
    @Test
    public void typeAttachmentCaseInsensitive() throws Exception {
        Assertions.assertTrue(isAttachment("AttachMent", "text/html"));
        Assertions.assertTrue(isAttachment("ATTACHMENT; FILENAME=foo.txt", "text/html"));
    }

    /**
     * White space around the type is ignored.
     * @throws Exception if an error occurs
     */
    @Test
    public void typeAttachmentWithWhitespace() throws Exception {
        Assertions.assertTrue(isAttachment("  attachment", "text/html"));
        Assertions.assertTrue(isAttachment(" attachment ; filename=foo.txt", "text/html"));
        Assertions.assertTrue(isAttachment("attachment ;filename=foo.txt", "text/html"));
    }

    /**
     * A type that only starts with attachment is an unknown type.
     * @throws Exception if an error occurs
     */
    @Test
    public void typeStartingWithAttachment() throws Exception {
        Assertions.assertTrue(isAttachment("attachmentfoo", "text/html"));
        Assertions.assertTrue(isAttachment("attachments; filename=foo.txt", "text/html"));
    }

    /**
     * Unknown types are handled like attachment (RFC 6266).
     * @throws Exception if an error occurs
     */
    @Test
    public void unknownType() throws Exception {
        Assertions.assertTrue(isAttachment("foo", "text/html"));
        Assertions.assertTrue(isAttachment("foo; filename=\"foo.txt\"", "text/html"));
        Assertions.assertTrue(isAttachment(" Foo ; filename=foo.txt", "text/html"));
    }

    /**
     * The type inline is no attachment.
     * @throws Exception if an error occurs
     */
    @Test
    public void typeInline() throws Exception {
        Assertions.assertFalse(isAttachment("inline", "text/html"));
        Assertions.assertFalse(isAttachment("INLINE; filename=foo.txt", "text/html"));
    }

    /**
     * The word attachment as value of a parameter does not make an attachment.
     * @throws Exception if an error occurs
     */
    @Test
    public void attachmentOnlyAsParameterValue() throws Exception {
        Assertions.assertFalse(isAttachment("inline; filename=attachment", "text/html"));
        Assertions.assertFalse(isAttachment("inline; name=\"attachment; filename=x\"", "text/html"));
    }

    /**
     * An empty header is no attachment.
     * @throws Exception if an error occurs
     */
    @Test
    public void emptyHeader() throws Exception {
        Assertions.assertFalse(isAttachment("", "text/html"));
        Assertions.assertFalse(isAttachment("   ", "text/html"));
    }

    /**
     * A header with a filename but without a type is no attachment.
     * @throws Exception if an error occurs
     */
    @Test
    public void filenameWithoutType() throws Exception {
        Assertions.assertFalse(isAttachment("; filename=foo.txt", "text/html"));
    }

    /**
     * The content type octet-stream is handled like an attachment, if there is no header.
     * @throws Exception if an error occurs
     */
    @Test
    public void octetStreamWithoutHeader() throws Exception {
        Assertions.assertTrue(isAttachment(null, "application/octet-stream"));
        Assertions.assertTrue(isAttachment(null, "Application/Octet-Stream"));
    }

    /**
     * Other content types without header are no attachment.
     * @throws Exception if an error occurs
     */
    @Test
    public void otherContentTypeWithoutHeader() throws Exception {
        Assertions.assertFalse(isAttachment(null, "text/html"));
        Assertions.assertFalse(isAttachment(null, null));
    }

    /**
     * The content type octet-stream is always an attachment, the browsers can not display it.
     * @throws Exception if an error occurs
     */
    @Test
    public void octetStreamWithHeader() throws Exception {
        Assertions.assertTrue(isAttachment("attachment", "application/octet-stream"));
        Assertions.assertTrue(isAttachment("inline", "application/octet-stream"));
        Assertions.assertTrue(isAttachment("inline; filename=foo.bin", "application/octet-stream"));
        Assertions.assertTrue(isAttachment("foo", "application/octet-stream"));
        Assertions.assertTrue(isAttachment("", "application/octet-stream"));
    }
}
