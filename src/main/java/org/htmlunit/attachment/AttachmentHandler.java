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

import java.io.Serializable;

import org.htmlunit.HttpHeader;
import org.htmlunit.Page;
import org.htmlunit.WebResponse;
import org.htmlunit.util.MimeType;
import org.htmlunit.util.StringUtils;

/**
 * <p>A handler for attachments, which represent pages received from the server which contain
 * {@code Content-Disposition=attachment} headers. Normally pages are loaded inline: clicking on
 * a link, for example, loads the linked page in the current window. Attached pages are different
 * in that they are intended to be loaded outside of this flow: clicking on a link prompts the
 * user to either save the linked page, or open it outside the current window, but does not
 * load the page in the current window.</p>
 *
 * <p>HtmlUnit complies with the semantics described above when an <code>AttachmentHandler</code> has
 * been registered with the {@link org.htmlunit.WebClient} via
 * {@link org.htmlunit.WebClient#setAttachmentHandler(AttachmentHandler)}. When
 * no attachment handler has been registered with the <code>WebClient</code>, the semantics described
 * above to not apply, and attachments are loaded inline. By default, <code>AttachmentHandler</code>s
 * are not registered with new <code>WebClient</code> instances.</p>
 *
 * @author Bruce Chapman
 * @author Sudhan Moghe
 * @author Daniel Gredler
 * @author Ronald Brill
 * @author Alex Gorbatovsky
 * @author Lai Quang Duong
 * @see org.htmlunit.WebClient#setAttachmentHandler(AttachmentHandler)
 * @see org.htmlunit.WebClient#getAttachmentHandler()
 * @see <a href="http://www.ietf.org/rfc/rfc2183.txt">RFC 2183</a>
 */
public interface AttachmentHandler extends Serializable {

    /**
     * Handles the specified attached page. This is some kind of information
     * that the page was handled as attachment.
     * This method will only be called if {@link #handleAttachment(WebResponse, String)}
     * has returned false for the response.
     * @param page an attached page, which doesn't get loaded inline
     * @param attachmentFilename the filename to use for the attachment or {@code null} if unspecified
     */
    void handleAttachment(Page page, String attachmentFilename);

    /**
     * Process the specified attachment. If this method returns false,
     * the client will open a new window with a page created from this
     * response as content.
     * Overwrite this method (and return true) if you do not need to create
     * a new window for the response.
     * @param response the response to process
     * @param attachmentFilename the filename to use for the attachment or {@code null} if unspecified
     * @return {@code true} if the specified response represents is handled by this method
     * @see <a href="http://www.ietf.org/rfc/rfc2183.txt">RFC 2183</a>
     */
    default boolean handleAttachment(final WebResponse response, final String attachmentFilename) {
        return false;
    }

    /**
     * Returns {@code true} if the specified response represents an attachment.
     * @param response the response to check
     * @return {@code true} if the specified response represents an attachment, {@code false} otherwise
     * @see <a href="http://www.ietf.org/rfc/rfc2183.txt">RFC 2183</a>
     */
    default boolean isAttachment(final WebResponse response) {
        final String disp = response.getResponseHeaderValue(HttpHeader.CONTENT_DISPOSITION);
        if (disp == null) {
            // if there is no content disposition header and content type 'application/octet-stream'
            // is handled like an attachment by the browsers
            // https://developer.mozilla.org/en-US/docs/Web/HTTP/Basics_of_HTTP/MIME_types#applicationoctet-stream
            // They treat it as if the Content-Disposition header was set to attachment, and propose a "Save As" dialog.
            final String contentType = response.getResponseHeaderValue(HttpHeader.CONTENT_TYPE);
            return MimeType.APPLICATION_OCTET_STREAM.equalsIgnoreCase(contentType);
        }
        return StringUtils.startsWithIgnoreCase(disp, "attachment");
    }
}
